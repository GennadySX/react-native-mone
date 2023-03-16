package com.mone;

import android.os.Bundle;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.annotations.ReactPropGroup;
import com.mone.modules.ImageFilters;
import com.mone.views.ResizeImageFragment;

import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.Objects;


public class MoneViewManager extends ViewGroupManager<FrameLayout>  {

  // Module name for React Native
  public static final String MODULE_NAME = "MoneView";

  // Command IDs for React Native by default always start from 1
  public final int COMMAND_CREATE = 1;

  // View width size parameter
  private int propWidth;

  // View height size parameter
  private int propHeight;

  // View border radius size parameter
  private int borderRadius = 0;

  // React application context as a member variable for accessing React Native modules
  private final ReactApplicationContext context;



  // Fragment instance for displaying image and applying parameters
  private ResizeImageFragment myFragment;

  // Bundle for passing arguments to fragment
  Bundle mArguments;

  /**
   * ResizeImageManager constructor
   * @param reactContext ReactApplicationContext instance
   */
  public MoneViewManager(ReactApplicationContext reactContext) {
    context = reactContext;
  }

  /**
   * Get module name
   * @return module name as string
   */
  @NonNull
  @Override
  public String getName() {
    return MODULE_NAME;
  }

  /**
   * FrameLayout view instance creation method for React Native view manager class (ResizeImageManager)
   * @param reactContext ThemedReactContext instance
   * @return FrameLayout view instance
   */
  @NonNull
  @Override
  public FrameLayout createViewInstance(@NonNull ThemedReactContext reactContext) {
    return new FrameLayout(reactContext);
  }


  /**
   * ReactCommand method for getting commands map
   * @return commands map
   */
  @Nullable
  @Override
  public Map<String, Integer> getCommandsMap() {
    return MapBuilder.of(
      "create", COMMAND_CREATE
    );
  }



  /**
   * Default ReactPropGroup method for width and height props
   * @param view - root view
   * index - index of prop
   * 0 - width
   * 1 - height
   */
  @ReactPropGroup(names = {"width", "height", "borderRadius"}, customType = "Style")
  public void setStyle(FrameLayout view, int index, Integer value) {
    if (index == 0) {
      propWidth = value;
    }

    if (index == 1) {
      propHeight = value;
    }

    if (index == 2) {
      borderRadius = value;
    }
  }


  /**
   * ReactProp method for source prop
   * @param view - root view
   * @param source - source prop
   */
  @ReactProp(name = "source") // <-- This is the method that gets called when the source prop is set
  public void setSource(FrameLayout view, @Nullable ReadableMap source) {
    assert source != null;
    // Get source arguments
    String uri = source.getString("uri");
    int width = source.getInt("width");
    int height = source.getInt("height");
    int borderRadius = source.getInt("borderRadius");

    String filterString = Objects.requireNonNull(source.getString("filter"));


    // Save arguments to use them later in createFragment method
    mArguments = new Bundle();
    mArguments.putString("uri", uri);
    mArguments.putInt("width", width);
    mArguments.putInt("height", height);
    mArguments.putString("filter", filterString);
    mArguments.putString("borderRadius", String.valueOf(borderRadius));
    mArguments.putInt("borderRadius", borderRadius);



    ImageFilters filter = ImageFilters.valueOf(filterString);

    // Call resize method if fragment is not null
    if (myFragment != null) {
      myFragment.resizeImageView.onChangeParams(uri, width, height, filter, borderRadius);
    }
  }

  /**
   * Get Bundle arguments
   */
  private Bundle getArguments() {
    return mArguments;
  }

  /**
   * Handle "create" command (called from JS) and call createFragment method
   * @param root - root view
   * @param commandId - command id
   * @param args - arguments
   */
  @Override
  public void receiveCommand(
    @NonNull FrameLayout root,
    String commandId,
    @Nullable ReadableArray args
  ) {

    super.receiveCommand(root, commandId, args);
    assert args != null;
    int reactNativeViewId = args.getInt(0);

    int commandIdInt = Integer.parseInt(commandId);

    if (commandIdInt == COMMAND_CREATE) {
      createFragment(root, reactNativeViewId);
    }

  }



  /**
   * Create fragment and add it to the activity
   * @param parentLayout - root view
   * @param reactNativeViewId - react native view id
   */
  public void createFragment(FrameLayout parentLayout, int reactNativeViewId) {
    // Parent view for fragment (root view)
    View parentView = (ViewGroup) parentLayout.findViewById(reactNativeViewId).getParent();
    setupLayout(parentLayout);

    myFragment = new ResizeImageFragment(); // <-- Our custom fragment
    myFragment.setArguments(getArguments()); // <-- Pass arguments to fragment


    FragmentActivity activity = (FragmentActivity) context.getCurrentActivity();
    assert activity != null;
    activity
      .getSupportFragmentManager()
      .beginTransaction()
      .replace(reactNativeViewId, myFragment, String.valueOf(reactNativeViewId))
      .commit();
  }

  /**
   * Setup layout
   * @param view - root view
   */
  public void setupLayout(View view) {
    // Set view size to 0 to avoid layout issues
    Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
      @Override
      public void doFrame(long frameTimeNanos) {
        manuallyLayoutChildren(view);
        view.getViewTreeObserver().dispatchOnGlobalLayout();
        Choreographer.getInstance().postFrameCallback(this);
      }
    });
  }

  /**
   * Layout all children properly
   * @param view - root view
   */
  public void manuallyLayoutChildren(View view) {

    // Measure view with given width and height
    view.measure(
      View.MeasureSpec.makeMeasureSpec(propWidth, View.MeasureSpec.EXACTLY),
      View.MeasureSpec.makeMeasureSpec(propHeight, View.MeasureSpec.EXACTLY));

    view.layout(0, 0, propWidth, propHeight); // set position and size of view


  }
}
