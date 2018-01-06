package com.codenotfound.client;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

public class ClientCallbackHandler implements CallbackHandler {

  @Override
  public void handle(Callback[] callbacks)
      throws IOException, UnsupportedCallbackException {
    // Do nothing
  }
}
