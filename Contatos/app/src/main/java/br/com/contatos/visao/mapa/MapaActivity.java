package br.com.contatos.visao.mapa;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.contatos.R;
import br.com.contatos.infraestrutura.util.Constantes;
import br.com.contatos.infraestrutura.util.Util;
import br.com.contatos.infraestrutura.util.UtilValidacao;
import br.com.contatos.modelo.entidade.contato.Contato;
import br.com.contatos.visao.contato.ContatoActivity;

public class MapaActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,
                                                              GooglePlayServicesClient.OnConnectionFailedListener,
                                                              GoogleMap.OnMapLongClickListener {

    static final int LOADER_ENDERECO = 1;
    static final String EXTRA_ORIG = "orig";
    static final String EXTRA_DEST = "dest";


    EditText mEdtLocal;
    ImageButton mBtnBuscar;
    MessageDialogFragment mDialogEnderecos;
    TextView mTxtProgresso;
    LinearLayout mLayoutProgresso;

    private GoogleMap mGoogleMap;
    private LatLng    mOrigem;
    private LocationClient mLocationClient;

    private ArrayList<LatLng> mRota;
    private LoaderManager mLoaderManager;

    static final int LOADER_ROTA = 2;

    LatLng localizacaoSelecionada;

    List<Address> addresses;

    private Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment fragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mGoogleMap = fragment.getMap();
        mGoogleMap.setOnMapLongClickListener(this);
        mLocationClient = new LocationClient(this, this, this);


        mEdtLocal = (EditText) findViewById(R.id.edtLocal);
        mBtnBuscar = (ImageButton) findViewById(R.id.imgBtnBuscar);
        mBtnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnBuscar.setEnabled(false);
                buscarEndereco();
            }
        });
        mLoaderManager = getSupportLoaderManager();
        mTxtProgresso = (TextView) findViewById(R.id.txtProgresso);
        mLayoutProgresso = (LinearLayout) findViewById(R.id.llProgresso);

        contato = new Contato();

        Intent intent = getIntent();
        contato.setCodigo(intent.getLongExtra(Constantes.ID_CONTATO,Constantes.PARAMETRO_NAO_ENCONTRADO));
        contato.setNome(intent.getStringExtra(Constantes.NOME));
        contato.setTelefone(intent.getStringExtra(Constantes.TELEFONE));
        contato.setIdUsuario(intent.getLongExtra(Constantes.ID_USUARIO,Constantes.PARAMETRO_NAO_ENCONTRADO));

        /**
         * Se for uma ediçao o codigo do contato  será maior que -1 (PARAMETRO_NAO_ENCONTRADO)
         */
        //if (contato.getCodigo() > Constantes.PARAMETRO_NAO_ENCONTRADO) {
            Bundle bundle = getIntent().getExtras();
            contato.setLongitude(bundle.getDouble(Constantes.LONGITUDE));
            contato.setLatitude(bundle.getDouble(Constantes.LATITUDE));
            contato.setDescricaoLocalizacao(intent.getStringExtra(Constantes.DESCRICAOLOCALIZACAO));
        Log.i("Coordenada - MapaActivity","Latitude: " + contato.getLatitude() + " - " +  "Longitude: " + contato.getLongitude());

        if (contato.getLatitude() > 0 || contato.getLongitude() > 0){
                LatLng coordenadaLocalizacao = new LatLng(contato.getLatitude(),contato.getLongitude());
                inicializarEndereco(coordenadaLocalizacao);
            }

            mEdtLocal.setText(contato.getDescricaoLocalizacao());

            if (contato.getLatitude() == 0 && contato.getLongitude() == 0 &&
                UtilValidacao.verificaCampoTextoNaoVazio(mEdtLocal.getText().toString())){
                buscarEndereco();
            }

            Log.i("Contato","No mapa Activity " + contato.toString());
            localizacaoSelecionada = new LatLng(contato.getLatitude(),contato.getLongitude());

            atualizaMapa();
        //}
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PlayServicesUtils.REQUEST_CODE_ERRO_PLAY_SERVICES && resultCode == RESULT_OK) {
            mLocationClient.connect();
        }
    }

    LoaderManager.LoaderCallbacks<List<Address>> mBuscaLocalCallback =
            new LoaderManager.LoaderCallbacks<List<Address>>() {
                @Override
                public Loader<List<Address>> onCreateLoader(int i, Bundle bundle) {
                    return new BuscarLocalTask(MapaActivity.this, mEdtLocal.getText().toString());
                }

                @Override
                public void onLoadFinished(Loader<List<Address>> listLoader,
                                           final List<Address> addresses) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            exibirListaEnderecos(addresses);
                        }
                    });
                }

                @Override
                public void onLoaderReset(Loader<List<Address>> listLoader) {
                }
            };

    @Override
    public void onConnected(Bundle dataBundle) {
        Log.d("Dominando", "onConnected");
        Location location = mLocationClient.getLastLocation();
        if (location != null) {
            mOrigem = new LatLng(location.getLatitude(), location.getLongitude());
            atualizaMapa();
        }

        if (estaCarregando(LOADER_ENDERECO) && localizacaoSelecionada == null) {
            mLoaderManager.initLoader(LOADER_ENDERECO, null, mBuscaLocalCallback);
            exibirProgresso("Buscando endereço...");

        }
    }

    private void atualizaMapa(){
        mGoogleMap.clear();

        if (localizacaoSelecionada != null) {
            mGoogleMap.addMarker(new MarkerOptions().position(localizacaoSelecionada));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(localizacaoSelecionada, 17.0f));
            inicializarEndereco(localizacaoSelecionada);
        }else{
            mGoogleMap.addMarker(new MarkerOptions().position(mOrigem).title("Origem"));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mOrigem, 17.0f));
        }
    }


    @Override
    public void onDisconnected() {
        Log.d("Dominando", "onDisconnected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this,
                        PlayServicesUtils.REQUEST_CODE_ERRO_PLAY_SERVICES);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            PlayServicesUtils.exibirMensagemDeErro(this, connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_ORIG, mOrigem);
        outState.putParcelable(EXTRA_DEST, localizacaoSelecionada);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mOrigem = savedInstanceState.getParcelable(EXTRA_ORIG);
            localizacaoSelecionada = savedInstanceState.getParcelable(EXTRA_DEST);
        }
    }

    private void buscarEndereco() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEdtLocal.getWindowToken(), 0);

        mLoaderManager.restartLoader(LOADER_ENDERECO, null, mBuscaLocalCallback);
        exibirProgresso("Procurando endereço...");
    }

    private void exibirProgresso(String texto) {
        mTxtProgresso.setText(texto);
        mLayoutProgresso.setVisibility(View.VISIBLE);
    }

    private void ocultarProgresso() {
        mLayoutProgresso.setVisibility(View.GONE);
    }

    private void exibirListaEnderecos(final List<Address> enderecosEncontrados) {

        ocultarProgresso();
        mBtnBuscar.setEnabled(true);

        if (enderecosEncontrados != null && enderecosEncontrados.size() > 0) {
            String[] descricaoDosEnderecos = new String[enderecosEncontrados.size()];
            for (int i = 0; i < descricaoDosEnderecos.length; i++) {
                Address endereco = enderecosEncontrados.get(i);

                StringBuffer rua = new StringBuffer();
                for (int j = 0; j < endereco.getMaxAddressLineIndex(); j++) {
                    if (rua.length() > 0) {
                        rua.append('\n');
                    }
                    rua.append(endereco.getAddressLine(j));
                }

                String pais = endereco.getCountryName();

                String descricaoEndereco = String.format(
                        "%s, %s", rua, pais);

                descricaoDosEnderecos[i] = descricaoEndereco;
            }

            DialogInterface.OnClickListener selecionarEnderecoClick =
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Address enderecoSelecionado = enderecosEncontrados.get(which);
                            localizacaoSelecionada = new LatLng(
                                    enderecoSelecionado.getLatitude(),
                                    enderecoSelecionado.getLongitude());
                            atualizaMapa();

                            //carregarRota();
                        }
                    };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Selecione o destino")
                    .setItems(descricaoDosEnderecos, selecionarEnderecoClick);

            mDialogEnderecos = new MessageDialogFragment();
            mDialogEnderecos.setDialog(builder.create());
            mDialogEnderecos.show(getSupportFragmentManager(), "DIALOG_ENDERECO_DESTINO");
        }
    }


    private boolean estaCarregando(int id) {
        Loader<?> loader = mLoaderManager.getLoader(id);
        if (loader != null && loader.isStarted()) {
            return true;
        }
        return false;
    }

    LoaderManager.LoaderCallbacks<List<LatLng>> mRotaCallback =
            new LoaderManager.LoaderCallbacks<List<LatLng>>() {

                @Override
                public Loader<List<LatLng>> onCreateLoader(int i, Bundle bundle) {
                    return null;
                }

                @Override
                public void onLoadFinished(final Loader<List<LatLng>> listLoader,
                                           final List<LatLng> latLngs) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mRota = new ArrayList<LatLng>(latLngs);
                            atualizaMapa();
                            ocultarProgresso();
                        }
                    });
                }

                @Override
                public void onLoaderReset(Loader<List<LatLng>> listLoader) {
                }
            };

    @Override
    public void onMapLongClick(LatLng latLng) {
        this.localizacaoSelecionada = latLng;
        inicializarEndereco(latLng);
        atualizaMapa();
    }

    private void inicializarEndereco(LatLng latLng){
        Geocoder geocoder  = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addresses.size() > 0) {
                mEdtLocal.setText(Util.formataEndereco(addresses.get(0)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void selecionaCoordenada(View view){

        if (addresses != null && UtilValidacao.verificaCampoTextoNaoVazio(Util.formataEndereco(addresses.get(0)))){
            contato.setDescricaoLocalizacao(Util.formataEndereco(addresses.get(0)));
            contato.setLatitude(addresses.get(0).getLatitude());
            contato.setLongitude(addresses.get(0).getLongitude());
            chamarTelaContato(true);
        }else{
            chamarTelaContato(true);
        }

//        if (localizacaoSelecionada != null) {
//            this.latitude = localizacaoSelecionada.latitude;
//            this.longitude = localizacaoSelecionada.longitude;
//            chamarTelaContato(true);
//        }else{
//            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
//            dialogo.setTitle(getString(R.string.selecaoLocalizacao));
//            dialogo.setMessage(getString(R.string.localizacaoNaoSelecionada));
//            dialogo.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface arg0, int arg1) {
//                    arg0.dismiss();
//                }
//            });
//            dialogo.create().show();
//        }

    }

    public void voltar(View view){
        chamarTelaContato(false);
    }

    private void chamarTelaContato(boolean editado){
        Intent intent = new Intent(this, ContatoActivity.class);
        intent.putExtra(Constantes.NOME,contato.getNome());
        intent.putExtra(Constantes.TELEFONE,contato.getTelefone());
        intent.putExtra(Constantes.DESCRICAOLOCALIZACAO,mEdtLocal.getText().toString());
        intent.putExtra(Constantes.ID_CONTATO, contato.getCodigo());
        intent.putExtra(Constantes.ID_USUARIO, contato.getIdUsuario());
        intent.putExtra(Constantes.EDITADO,true);
        intent.putExtra(Constantes.RETORNOUTELALOCALIZACAO,true);

        Bundle bundle = new Bundle();
        bundle.putDouble(Constantes.LATITUDE, contato.getLatitude());
        bundle.putDouble(Constantes.LONGITUDE, contato.getLongitude());
        intent.putExtras(bundle);

        startActivity(intent);
        finish();
    }
}
