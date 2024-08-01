package nie.translator.rtranslator.voice_translation._text_translation;

import static androidx.core.content.ContextCompat.getSystemService;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import nie.translator.rtranslator.Global;
import nie.translator.rtranslator.R;
import nie.translator.rtranslator.bluetooth.Message;
import nie.translator.rtranslator.settings.SettingsActivity;
import nie.translator.rtranslator.tools.CustomLocale;
import nie.translator.rtranslator.tools.ErrorCodes;
import nie.translator.rtranslator.tools.Tools;
import nie.translator.rtranslator.tools.gui.AnimatedTextView;
import nie.translator.rtranslator.tools.gui.GuiTools;
import nie.translator.rtranslator.tools.gui.LanguageListAdapter;
import nie.translator.rtranslator.tools.gui.animations.CustomAnimator;
import nie.translator.rtranslator.tools.gui.messages.GuiMessage;
import nie.translator.rtranslator.voice_translation.VoiceTranslationActivity;
import nie.translator.rtranslator.voice_translation.neural_networks.translation.Translator;

public class TranslationFragment extends Fragment {
    private VoiceTranslationActivity activity;
    private Global global;
    private Translator.TranslateListener translateListener;
    private TextWatcher inputTextListener;
    private TextWatcher outputTextListener;

    //TranslatorFragment's GUI
    private MaterialButton translateButton;
    private FloatingActionButton walkieTalkieButton;
    private FloatingActionButton conversationButton;
    private MaterialButton walkieTalkieButtonSmall;
    private MaterialButton conversationButtonSmall;
    private TextView walkieTalkieButtonText;
    private TextView conversationButtonText;
    private EditText inputText;
    private EditText outputText;
    private ConstraintLayout firstLanguageSelector;
    private ConstraintLayout secondLanguageSelector;
    private AppCompatImageButton invertLanguagesButton;
    private View lineSeparator;
    private ConstraintLayout toolbarContainer;
    private TextView title;
    private AppCompatImageButton settingsButton;
    private AppCompatImageButton settingsButtonReduced;
    private AppCompatImageButton backButton;
    private CustomAnimator animator = new CustomAnimator();
    private Animator colorAnimator = null;
    private int activatedColor = R.color.primary;
    private int deactivatedColor = R.color.gray;
    private boolean isKeyboardShowing = false;
    ViewTreeObserver.OnGlobalLayoutListener layoutListener;

    //languageListDialog
    private LanguageListAdapter listView;
    private ListView listViewGui;
    private ProgressBar progressBar;
    private ImageButton reloadButton;
    private AlertDialog dialog;
    public static final int BEAM_SIZE = 1;

    //animations
    private int textActionButtonHeight;
    private int textActionButtonBottomMargin;
    private int actionButtonHeight;
    private int translateButtonHeight;
    private int actionButtonTopMargin;
    private int actionButtonBottomMargin;
    @Nullable
    private Animator animationKeyboardButton;
    @Nullable
    private Animator animationKeyboardTop;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_translation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstLanguageSelector = view.findViewById(R.id.firstLanguageSelector);
        secondLanguageSelector = view.findViewById(R.id.secondLanguageSelector);
        invertLanguagesButton = view.findViewById(R.id.invertLanguages);
        translateButton = view.findViewById(R.id.buttonTranslate);
        walkieTalkieButton = view.findViewById(R.id.button);
        conversationButton = view.findViewById(R.id.button2);
        walkieTalkieButtonSmall = view.findViewById(R.id.buttonWalkieTalkieSmall);
        conversationButtonSmall = view.findViewById(R.id.buttonConversationSmall);
        walkieTalkieButtonText = view.findViewById(R.id.textButton1);
        conversationButtonText = view.findViewById(R.id.textButton2);
        inputText = view.findViewById(R.id.multiAutoCompleteTextView);
        outputText = view.findViewById(R.id.multiAutoCompleteTextView2);
        lineSeparator = view.findViewById(R.id.lineSeparator);
        toolbarContainer = view.findViewById(R.id.toolbarTranslatorContainer);
        title = view.findViewById(R.id.title2);
        settingsButton = view.findViewById(R.id.settingsButton);
        settingsButtonReduced = view.findViewById(R.id.settingsButton2);
        backButton = view.findViewById(R.id.backButton);
        //we set the listener for the keyboard opening
        // ContentView is the root view of the layout of this activity/fragment
        layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(translateButtonHeight == 0){
                    //we set the animations parameters
                    textActionButtonHeight = walkieTalkieButtonText.getHeight();
                    textActionButtonBottomMargin = ((ConstraintLayout.LayoutParams) walkieTalkieButtonText.getLayoutParams()).bottomMargin;
                    actionButtonHeight = walkieTalkieButton.getHeight();
                    translateButtonHeight = translateButton.getHeight();
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) walkieTalkieButton.getLayoutParams();
                    actionButtonTopMargin = layoutParams.topMargin;
                    actionButtonBottomMargin = layoutParams.bottomMargin;
                } else {  //we start detecting keyboard only when the view is rendered (we use the translateButtonHeight to detect that)
                    Rect r = new Rect();
                    view.getWindowVisibleDisplayFrame(r);
                    int screenHeight = view.getRootView().getHeight();

                    // r.bottom is the bottom position of the window of the fragment (number of pixels from the top of the screen).
                    // keyboardHeight is the difference between screenHeight (pixels from top to bottom of the screen) and r.button (pixels from the top of the screen and the bottom of the window of the Fragment).
                    int keyboardHeight = screenHeight - r.bottom;

                    Log.d("keyboard", "keypadHeight = " + keyboardHeight);

                    if (keyboardHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keyboard height.
                        // keyboard is opened
                        if (!isKeyboardShowing) {
                            isKeyboardShowing = true;
                            onKeyboardVisibilityChanged(true);
                        }
                    } else {
                        // keyboard is closed
                        if (isKeyboardShowing) {
                            isKeyboardShowing = false;
                            onKeyboardVisibilityChanged(false);
                        }
                    }
                }
            }
        };
        view.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (VoiceTranslationActivity) requireActivity();
        global = (Global) activity.getApplication();
        Toolbar toolbar = activity.findViewById(R.id.toolbarTranslator);
        activity.setActionBar(toolbar);
        //inputText.setImeOptions(EditorInfo.IME_ACTION_GO);
        //inputText.setRawInputType(InputType.TYPE_CLASS_TEXT);

        // setting of the selected languages
        global.getFirstLanguage(true, new Global.GetLocaleListener() {
            @Override
            public void onSuccess(CustomLocale result) {
                setFirstLanguage(result);
            }
            @Override
            public void onFailure(int[] reasons, long value) {

            }
        });
        global.getSecondLanguage(true, new Global.GetLocaleListener() {
            @Override
            public void onSuccess(CustomLocale result) {
                setSecondLanguage(result);
            }
            @Override
            public void onFailure(int[] reasons, long value) {

            }
        });
        walkieTalkieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setFragment(VoiceTranslationActivity.WALKIE_TALKIE_FRAGMENT);
            }
        });
        conversationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setFragment(VoiceTranslationActivity.PAIRING_FRAGMENT);
            }
        });
        walkieTalkieButtonSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setFragment(VoiceTranslationActivity.WALKIE_TALKIE_FRAGMENT);
            }
        });
        conversationButtonSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setFragment(VoiceTranslationActivity.PAIRING_FRAGMENT);
            }
        });
        translateListener = new Translator.TranslateListener() {
            @Override
            public void onTranslatedText(String text, long resultID, boolean isFinal, CustomLocale languageOfText) {
                outputText.setText(text);
                if(isFinal){
                    activateTranslationButton();
                }
            }

            @Override
            public void onFailure(int[] reasons, long value) {

            }
        };
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = inputText.getText().toString();

                /*if(text.length() <= 0){   //test code
                    text = "Also unlike 2014, there aren’t nearly as many loopholes. You can’t just buy a 150-watt incandescent or a three-way bulb, the ban covers any normal bulb that generates less than 45 lumens per watt, which pretty much rules out both incandescent and halogen tech in their entirety.";
                    inputText.setText(text);
                }*/

                if(!text.isEmpty()) {
                    String finalText = text;
                    global.getFirstAndSecondLanguages(true, new Global.GetTwoLocaleListener() {
                        @Override
                        public void onSuccess(CustomLocale firstLanguage, CustomLocale secondLanguage) {
                            //we deactivate translate button
                            deactivateTranslationButton();
                            //we start the translation
                            global.getTranslator().translate(finalText, firstLanguage, secondLanguage, BEAM_SIZE, true);
                        }

                        @Override
                        public void onFailure(int[] reasons, long value) {

                        }
                    });
                }
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        settingsButtonReduced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void onStart() {
        super.onStart();
        GuiMessage lastInputText = global.getTranslator().getLastInputText();
        GuiMessage lastOutputText = global.getTranslator().getLastOutputText();

        outputTextListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(lineSeparator.getVisibility() != View.VISIBLE){
                    lineSeparator.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        outputText.addTextChangedListener(outputTextListener);

        //we restore the last input and output text
        if(lastInputText != null){
            inputText.setText(lastInputText.getMessage().getText());
        }
        if(lastOutputText != null){
            outputText.setText(lastOutputText.getMessage().getText());
        }
        //we attach the translate listener
        global.getTranslator().addCallback(translateListener);
        //we attach the click listener for the language selectors
        firstLanguageSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageListDialog(1);
            }
        });
        secondLanguageSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageListDialog(2);
            }
        });
        invertLanguagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                global.getFirstAndSecondLanguages(true, new Global.GetTwoLocaleListener() {
                    @Override
                    public void onSuccess(CustomLocale language1, CustomLocale language2) {
                        setFirstLanguage(language2);
                        setSecondLanguage(language1);
                    }

                    @Override
                    public void onFailure(int[] reasons, long value) {

                    }
                });
            }
        });
        inputTextListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(global.getTranslator() != null){
                    global.getTranslator().setLastInputText(new GuiMessage(new Message(global, s.toString()), true, true));
                }
            }
        };
        inputText.addTextChangedListener(inputTextListener);
        inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we start the compress animations
                /*if (!isKeyboardShowing) {
                    isKeyboardShowing = true;
                    onKeyboardVisibilityChanged(true);
                }*/
            }
        });
        //we set the option to not compress ui when the keyboard is shown
        //activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //we restore the translation button state based on the translation status
        if(global.getTranslator().isTranslating()){
            deactivateTranslationButton();
        }else{
            activateTranslationButton();
        }
    }

    private void onKeyboardVisibilityChanged(boolean opened) {
        if(activity != null) {
            if (animationKeyboardButton != null) {
                animationKeyboardButton.cancel();
            }
            if(animationKeyboardTop != null){
                animationKeyboardTop.cancel();
            }
            if (opened) {
                animationKeyboardButton = animator.animateTranslationButtonsCompress(activity, this, walkieTalkieButton, walkieTalkieButtonText, conversationButton, conversationButtonText, walkieTalkieButtonSmall, conversationButtonSmall, new CustomAnimator.Listener() {
                    @Override
                    public void onAnimationEnd() {
                        super.onAnimationEnd();
                        animationKeyboardButton = null;
                    }
                });
                animationKeyboardTop = animator.animateCompressActionBar(activity, toolbarContainer, title, settingsButton, settingsButtonReduced, backButton, new CustomAnimator.Listener() {
                    @Override
                    public void onAnimationEnd() {
                        super.onAnimationEnd();
                        animationKeyboardTop = null;
                    }
                });
            } else {
                animationKeyboardButton = animator.animateTranslationButtonsEnlarge(activity, this, walkieTalkieButton, walkieTalkieButtonText, conversationButton, conversationButtonText, walkieTalkieButtonSmall, conversationButtonSmall, new CustomAnimator.Listener() {
                    @Override
                    public void onAnimationEnd() {
                        super.onAnimationEnd();
                        animationKeyboardButton = null;
                    }
                });
                animationKeyboardTop = animator.animateEnlargeActionBar(activity, toolbarContainer, title, settingsButton, settingsButtonReduced, backButton, new CustomAnimator.Listener() {
                    @Override
                    public void onAnimationEnd() {
                        super.onAnimationEnd();
                        animationKeyboardTop = null;
                    }
                });
            }
        }
    }

    private void activateTranslationButton(){
        if(colorAnimator != null){
            colorAnimator.cancel();
        }
        if(!translateButton.isActivated()) {
            colorAnimator = animator.createAnimatorColor(translateButton, GuiTools.getColorStateList(activity, deactivatedColor).getDefaultColor(), GuiTools.getColorStateList(activity, activatedColor).getDefaultColor(), activity.getResources().getInteger(R.integer.durationShort));
            colorAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animation) {
                }

                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    translateButton.setActivated(true);
                    colorAnimator = null;
                }

                @Override
                public void onAnimationCancel(@NonNull Animator animation) {
                }

                @Override
                public void onAnimationRepeat(@NonNull Animator animation) {
                }
            });
            colorAnimator.start();
        }else{
            translateButton.setBackgroundColor(GuiTools.getColorStateList(activity, activatedColor).getDefaultColor());
        }
    }

    private void deactivateTranslationButton(){
        if(colorAnimator != null){
            colorAnimator.cancel();
        }
        if(translateButton.isActivated()) {
            translateButton.setActivated(false);
            colorAnimator = animator.createAnimatorColor(translateButton, GuiTools.getColorStateList(activity, activatedColor).getDefaultColor(), GuiTools.getColorStateList(activity, deactivatedColor).getDefaultColor(), activity.getResources().getInteger(R.integer.durationShort));
            colorAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animation) {
                }

                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    colorAnimator = null;
                }

                @Override
                public void onAnimationCancel(@NonNull Animator animation) {
                }

                @Override
                public void onAnimationRepeat(@NonNull Animator animation) {
                }
            });
            colorAnimator.start();
        }else{
            translateButton.setBackgroundColor(GuiTools.getColorStateList(activity, deactivatedColor).getDefaultColor());
        }
    }

    private void showLanguageListDialog(final int languageNumber) {
        //when the dialog is shown at the beginning the loading is shown, then once the list of languages​is obtained (within the showList)
        //the loading is replaced with the list of languages
        String title = "";
        switch (languageNumber) {
            case 1: {
                title = global.getResources().getString(R.string.dialog_select_first_language);
                break;
            }
            case 2: {
                title = global.getResources().getString(R.string.dialog_select_second_language);
                break;
            }
        }

        final View editDialogLayout = activity.getLayoutInflater().inflate(R.layout.dialog_languages, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true);
        builder.setTitle(title);

        dialog = builder.create();
        dialog.setView(editDialogLayout, 0, Tools.convertDpToPixels(activity, 16), 0, 0);
        dialog.show();

        listViewGui = editDialogLayout.findViewById(R.id.list_view_dialog);
        progressBar = editDialogLayout.findViewById(R.id.progressBar3);
        reloadButton = editDialogLayout.findViewById(R.id.reloadButton);

        Global.GetLocaleListener listener = new Global.GetLocaleListener() {
            @Override
            public void onSuccess(final CustomLocale result) {
                reloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showList(languageNumber, result);
                    }
                });
                showList(languageNumber, result);
            }

            @Override
            public void onFailure(int[] reasons, long value) {
                onFailureShowingList(reasons, value);
            }
        };

        switch (languageNumber) {
            case 1: {
                global.getFirstLanguage(false, listener);
                break;
            }
            case 2: {
                global.getSecondLanguage(false, listener);
                break;
            }
        }
    }

    private void showList(final int languageNumber, final CustomLocale selectedLanguage) {
        reloadButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        global.getLanguages(true, true, new Global.GetLocalesListListener() {
            @Override
            public void onSuccess(final ArrayList<CustomLocale> languages) {
                progressBar.setVisibility(View.GONE);
                listViewGui.setVisibility(View.VISIBLE);

                listView = new LanguageListAdapter(activity, false, languages, selectedLanguage);
                listViewGui.setAdapter(listView);
                listViewGui.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        if (languages.contains((CustomLocale) listView.getItem(position))) {
                            switch (languageNumber) {
                                case 1: {
                                    setFirstLanguage((CustomLocale) listView.getItem(position));
                                    break;
                                }
                                case 2: {
                                    setSecondLanguage((CustomLocale) listView.getItem(position));
                                    break;
                                }
                            }
                        }
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onFailure(int[] reasons, long value) {
                onFailureShowingList(reasons, value);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        firstLanguageSelector.setOnClickListener(null);
        secondLanguageSelector.setOnClickListener(null);
        invertLanguagesButton.setOnClickListener(null);
        inputText.removeTextChangedListener(inputTextListener);
        outputText.removeTextChangedListener(outputTextListener);
        //we detach the translate listener
        global.getTranslator().removeCallback(translateListener);
    }

    @Override
    public void onDestroyView() {
        if(getView() != null) {
            getView().getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        }
        super.onDestroyView();
    }

    private void setFirstLanguage(CustomLocale language) {
        // save firstLanguage selected
        global.setFirstLanguage(language);
        // change language displayed
        ((AnimatedTextView) firstLanguageSelector.findViewById(R.id.firstLanguageName)).setText(language.getDisplayNameWithoutTTS(), true);
    }

    private void setSecondLanguage(CustomLocale language) {
        // save secondLanguage selected
        global.setSecondLanguage(language);
        // change language displayed
        ((AnimatedTextView) secondLanguageSelector.findViewById(R.id.secondLanguageName)).setText(language.getDisplayNameWithoutTTS(), true);
    }

    private void onFailureShowingList(int[] reasons, long value) {
        progressBar.setVisibility(View.GONE);
        reloadButton.setVisibility(View.VISIBLE);
        for (int aReason : reasons) {
            switch (aReason) {
                case ErrorCodes.MISSED_ARGUMENT:
                case ErrorCodes.SAFETY_NET_EXCEPTION:
                case ErrorCodes.MISSED_CONNECTION:
                    Toast.makeText(activity, getResources().getString(R.string.error_internet_lack_loading_languages), Toast.LENGTH_LONG).show();
                    break;
                default:
                    activity.onError(aReason, value);
                    break;
            }
        }
    }

    public int getTextActionButtonHeight() {
        return textActionButtonHeight;
    }

    public int getTextActionButtonBottomMargin() {
        return textActionButtonBottomMargin;
    }

    public int getActionButtonHeight() {
        return actionButtonHeight;
    }

    public int getTranslateButtonHeight(){
        return translateButtonHeight;
    }

    public int getActionButtonTopMargin() {
        return actionButtonTopMargin;
    }

    public int getActionButtonBottomMargin() {
        return actionButtonBottomMargin;
    }
}
