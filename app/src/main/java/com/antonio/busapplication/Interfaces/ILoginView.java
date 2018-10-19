package com.antonio.busapplication.Interfaces;

import android.widget.TextView;

/**
 * Created by Antonio on 5/27/2017.
 */

public interface ILoginView {
    void showUserView();
    void showAdminView();
    void showErrorMessage();

    String getEmail();

    String getPassword();

    TextView getEmailView();

    TextView getPasswordView();

    void showUserCreatedMessage();
}
