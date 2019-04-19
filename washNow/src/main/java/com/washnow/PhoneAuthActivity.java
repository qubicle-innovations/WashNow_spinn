package com.washnow;

/**
 * Created by aswin on 27/12/17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;
import com.washnow.utils.Utils;
import com.washnow.vo.OrderVo;
import com.washnow.vo.SignUpPojo;
import com.washnow.vo.UserVO;

import java.util.concurrent.TimeUnit;

/**
 * Created by AJ
 * Created on 09-Jun-17.
 */


public class PhoneAuthActivity extends BaseBackActivity implements
        View.OnClickListener {

    EditText mPhoneNumberField, mVerificationField;
    Button mStartButton, mVerifyButton, mResendButton;
    TextView mCountryCodeField;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    SignUpPojo pojo;
    Context context;
    private CountryPicker picker;

    private static final String TAG = "PhoneAuthActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        context = this;
        mPhoneNumberField = (EditText) findViewById(R.id.etPhone);
        mVerificationField = (EditText) findViewById(R.id.field_verification_code);
        mCountryCodeField = (TextView) findViewById(R.id.tvCountryCode);
        mStartButton = (Button) findViewById(R.id.button_start_verification);
        mVerifyButton = (Button) findViewById(R.id.button_verify_phone);
        mResendButton = (Button) findViewById(R.id.button_resend);
        pojo = (SignUpPojo) getIntent().getSerializableExtra("pojo");

        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);
        findViewById(R.id.llVerify).setVisibility(View.GONE);
        findViewById(R.id.llCode).setVisibility(View.INVISIBLE);
        setTitle("");
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mPhoneNumberField.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    findViewById(R.id.button_start_verification).setVisibility(View.VISIBLE);
                }
                Utils.setSharedPreference(context, "verified", "false");

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
                Utils.setSharedPreference(context, "verified", "false");

            }
        };
        picker = CountryPicker.newInstance("Select Country");
        Country country = picker.getUserCountryInfo(this);

        final TextView tvCountryCode = (TextView) findViewById(R.id.tvCountryCode);
        final ImageView imFlag = (ImageView) findViewById(R.id.imCountryFlag);

        if (country != null && country.getDialCode() != null && country.getFlag() > 0) {
            tvCountryCode.setText(country.getDialCode());
            imFlag.setImageResource(country.getFlag());
        }
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {

                tvCountryCode.setText(dialCode);
                imFlag.setImageResource(flagDrawableResID);
                picker.getSearchEditText().setText(null);
                picker.dismiss();
            }
        });
        tvCountryCode.setOnClickListener(this);
        imFlag.setOnClickListener(this);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String countryCode = mCountryCodeField.getText().toString().trim();
                            String phoneNumber = mPhoneNumberField.getText().toString().trim();
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            UserVO vo = pojo.getUser();
                            Utils.setSharedPreference(context, "name", vo.getName());
                            Utils.setSharedPreference(context, "email", vo.getEmail());
                            Utils.setSharedPreference(context, "phone", countryCode + phoneNumber);
                            Utils.setSharedPreference(context, "userid", vo.getUser_id());
                            Utils.setSharedPreference(context, "verified", "true");
                            if (pojo.getOrder() != null) {
                                OrderVo order = pojo.getOrder();
                                Utils.setOrder(context, order);
                                Intent intent = new Intent(context, OrderStatusActivity.class);
                                intent.putExtra("order", order);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                Intent intent = new Intent(context, RequestPickupActivity.class);

                                startActivity(intent);
                                finish();
                            }
                            Toast.makeText(context, "Succesfully Registered", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                mVerificationField.setError("Invalid code.");
                                Utils.setSharedPreference(context, "verified", "false");
                            }
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
      /*  FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(PhoneAuthActivity.this, RequestPickupActivity.class));
            finish();
        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start_verification:
                if (!validatePhoneNumber()) {
                    return;
                }
                String countryCode = mCountryCodeField.getText().toString().trim();
                String phoneNumber = mPhoneNumberField.getText().toString().trim();

                startPhoneNumberVerification(countryCode + phoneNumber);
                findViewById(R.id.button_start_verification).setVisibility(View.GONE);
                findViewById(R.id.llVerify).setVisibility(View.VISIBLE);
                findViewById(R.id.llCode).setVisibility(View.VISIBLE);
                Toast.makeText(context, "You will recieve verification code soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_verify_phone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.button_resend:
                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                break;
            case R.id.imCountryFlag:
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
                break;
            case R.id.tvCountryCode:
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
                break;

        }

    }

}