package es.dpinfo.spotlightplace.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.interfaces.IVerifyMvp;
import es.dpinfo.spotlightplace.presenters.VerifyNumPresenter;
import es.dpinfo.spotlightplace.schemas.SpotlightContract;

/**
 * Created by dprimenko on 3/01/17.
 */
public class AskNumberFragment extends Fragment implements IVerifyMvp.View {

    private EditText edtAskRegionCodeLogin;
    private EditText edtAskNumberLogin;
    private Button btnOkAskNumberLogin;
    private VerifyNumPresenter presenter;
    private RelativeLayout rlAskNumberLogin;
    private AlertDialog.Builder alertVerifyNumberBuilder;

    private AskNumberFragmentListener mCallback;

    public interface AskNumberFragmentListener {
        void onVerifyNumFragment(Bundle bundle);
    }

    public static AskNumberFragment newInstance() {
        return new AskNumberFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_ask_phone, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        View rootView = view;

        if (rootView != null) {
            edtAskRegionCodeLogin = (EditText) rootView.findViewById(R.id.edt_ask_region_code_login);
            edtAskRegionCodeLogin = (EditText) rootView.findViewById(R.id.edt_ask_region_code_login);
            edtAskNumberLogin = (EditText) rootView.findViewById(R.id.edt_ask_number_login);
            btnOkAskNumberLogin = (Button) rootView.findViewById(R.id.btn_ok_ask_number_login);
            rlAskNumberLogin = (RelativeLayout) rootView.findViewById(R.id.rl_ask_numberphone);
            setOnClickListeners();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new VerifyNumPresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (AskNumberFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AskNumberFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public void setOnClickListeners() {
        btnOkAskNumberLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (presenter.validateNumberPhone(edtAskRegionCodeLogin.getText().toString(), edtAskNumberLogin.getText().toString())) {
                    alertVerifyNumberBuilder = new AlertDialog.Builder(getActivity());
                    alertVerifyNumberBuilder.setMessage(getString(R.string.validate_phone_alert1) + "\n\n " +
                            "+" + edtAskRegionCodeLogin.getText().toString() + " " + edtAskNumberLogin.getText().toString() +
                            "\n\n" + getString(R.string.validate_phone_alert2)).
                            setCancelable(false).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Bundle bundle = new Bundle();
                            bundle.putString(SpotlightContract.UserEntry.KEY_NUMBER_PHONE, ("00" + edtAskRegionCodeLogin.getText().toString() + edtAskNumberLogin.getText().toString()));
                            bundle.putString("numberPhoneView", ("+ " + edtAskRegionCodeLogin.getText().toString() + " " +edtAskNumberLogin.getText().toString()));
                            mCallback.onVerifyNumFragment(bundle);
                        }
                    }).setNegativeButton(getString(R.string.edit), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            alertVerifyNumberBuilder.setCancelable(true);
                        }
                    });

                    AlertDialog alertVerifyNumber = alertVerifyNumberBuilder.create();
                    alertVerifyNumber.show();
                }
            }
        });
    }

    @Override
    public void setMessageError(int messageError) {
        Snackbar.make(rlAskNumberLogin, messageError, Snackbar.LENGTH_SHORT).show();
    }
}
