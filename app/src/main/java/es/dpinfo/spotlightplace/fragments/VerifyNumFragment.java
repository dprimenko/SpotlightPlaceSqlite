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
import android.widget.TextView;

import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.interfaces.IVerifyMvp;
import es.dpinfo.spotlightplace.presenters.VerifyNumPresenter;
import es.dpinfo.spotlightplace.schemas.SpotlightContract;

/**
 * Created by dprimenko on 3/01/17.
 */
public class VerifyNumFragment extends Fragment implements IVerifyMvp.View {

    private TextView txvNumberVeryfySmsCodeLogin;
    private EditText edtSecretCodeVerifySmsCodeLogin;
    private Button btnOkVerifySmsCodeLogin;
    private Button btnCheckSecretCode;
    private RelativeLayout rlVerifySmsCodeLogin;
    private AlertDialog.Builder alertCheckSecreCodeBuilder;
    private VerifyNumPresenter presenter;

    private VerifyNumFragmentListener mCallback;

    public interface VerifyNumFragmentListener {
        void onCodeVerified(Bundle bundle);
    }

    public static VerifyNumFragment newInstance(Bundle bundle) {
        VerifyNumFragment fragment = new VerifyNumFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new VerifyNumPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_verify_sms, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        View rootView = view;

        if (rootView != null) {

            txvNumberVeryfySmsCodeLogin = (TextView) rootView.findViewById(R.id.txv_number_veryfy_sms_code_login);
            edtSecretCodeVerifySmsCodeLogin = (EditText) rootView.findViewById(R.id.edt_secret_code_verify_sms_code_login);
            btnCheckSecretCode = (Button) rootView.findViewById(R.id.btnCheckSecretCode);
            btnOkVerifySmsCodeLogin = (Button) rootView.findViewById(R.id.btn_on_verify_sms_code_login);
            rlVerifySmsCodeLogin = (RelativeLayout) rootView.findViewById(R.id.rl_verify_sms_code_login);

            txvNumberVeryfySmsCodeLogin.setText(getArguments().getString("numberPhoneView"));

            btnCheckSecretCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertCheckSecreCodeBuilder = new AlertDialog.Builder(getActivity());

                    alertCheckSecreCodeBuilder.setCancelable(false)
                            .setMessage("Secret code: \n R4769")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertCheckSecreCodeBuilder.setCancelable(true);
                                }
                            });

                    AlertDialog alertCheckSecretCode = alertCheckSecreCodeBuilder.create();
                    alertCheckSecretCode.show();
                }
            });

            btnOkVerifySmsCodeLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String secretCode = edtSecretCodeVerifySmsCodeLogin.getText().toString();

                    if (presenter.validateSecretCode(secretCode)) {

                        Bundle bundle = new Bundle();
                        bundle.putString(SpotlightContract.UserEntry.KEY_NUMBER_PHONE, getArguments().getString(SpotlightContract.UserEntry.KEY_NUMBER_PHONE));
                        mCallback.onCodeVerified(bundle);
                    }
                    else {
                        edtSecretCodeVerifySmsCodeLogin.setText("");
                    }
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (VerifyNumFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AskNumberFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void setMessageError(int messageError) {
        Snackbar.make(rlVerifySmsCodeLogin, messageError, Snackbar.LENGTH_SHORT).show();
    }
}
