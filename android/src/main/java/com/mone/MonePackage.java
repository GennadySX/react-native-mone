/*
 * @author GennadySX
 * @created at 2023
 **/

package com.mone;

import androidx.annotation.NonNull;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MonePackage implements ReactPackage
{
  @NonNull
  @Override
  public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactApplicationContext) {
    List<NativeModule> modules = new ArrayList<>();
    modules.add(new MoneFilterModule(reactApplicationContext));
    return modules;
  }

  @NonNull
  @Override
  public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactApplicationContext) {
    return Arrays.<ViewManager>asList(
      new MoneViewManager(reactApplicationContext)
    );
  }
}
