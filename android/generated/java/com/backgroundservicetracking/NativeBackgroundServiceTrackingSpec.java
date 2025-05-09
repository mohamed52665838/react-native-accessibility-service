
/**
 * This code was generated by [react-native-codegen](https://www.npmjs.com/package/react-native-codegen).
 *
 * Do not edit this file as changes may cause incorrect behavior and will be lost
 * once the code is regenerated.
 *
 * @generated by codegen project: GenerateModuleJavaSpec.js
 *
 * @nolint
 */

package com.backgroundservicetracking;

import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.turbomodule.core.interfaces.TurboModule;
import javax.annotation.Nonnull;

public abstract class NativeBackgroundServiceTrackingSpec extends ReactContextBaseJavaModule implements TurboModule {
  public static final String NAME = "BackgroundServiceTracking";

  public NativeBackgroundServiceTrackingSpec(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public @Nonnull String getName() {
    return NAME;
  }

  protected final void emitIsServiceRunning(boolean value) {
    mEventEmitterCallback.invoke("isServiceRunning", value);
  }

  protected final void emitIsGlobalSettingsEnabled(boolean value) {
    mEventEmitterCallback.invoke("isGlobalSettingsEnabled", value);
  }

  @ReactMethod
  @DoNotStrip
  public abstract void startTrackingService(Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void stopTrackingService(Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void getPermissionToUseService(Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void checkPermissionForService(Promise promise);
}
