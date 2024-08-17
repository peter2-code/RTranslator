/*
 * Copyright 2016 Luca Martino.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copyFile of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nie.translator.rtranslator.tools.gui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import nie.translator.rtranslator.R;
import nie.translator.rtranslator.tools.gui.animations.CustomAnimator;
import nie.translator.rtranslator.voice_translation.VoiceTranslationActivity;
import nie.translator.rtranslator.voice_translation._conversation_mode._conversation.main.ConversationMainFragment;


public class ButtonMic extends DeactivableButton {
    public static final int SIZE_DEACTIVATED_DP = 56;
    public static final int SIZE_NORMAL_DP = 66;
    public static final int SIZE_LISTENING_DP = 76;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_RETURN = 1;
    public static final int STATE_SEND = 2;
    private boolean isMute = false;
    private int state = STATE_NORMAL;
    private boolean isListening = false;
    private TextView micInput;
    private EditText editText;
    @Nullable
    private MicrophoneComunicable fragment;
    private Context context;
    private CustomAnimator animator = new CustomAnimator();
    private ButtonMicColor currentColor;
    public static ButtonMicColor colorActivated;
    public static ButtonMicColor colorMutedActivated;
    public static ButtonMicColor colorDeactivated;
    public static ButtonMicColor colorMutedDeactivated;


    public ButtonMic(Context context) {
        super(context);
        this.context = context;
        colorActivated = new ButtonMicColor(GuiTools.getColorStateList(context,R.color.white), GuiTools.getColorStateList(context,R.color.primary));
        colorMutedActivated = new ButtonMicColor(GuiTools.getColorStateList(context,R.color.primary_very_dark), GuiTools.getColorStateList(context,R.color.primary_very_lite));
        colorDeactivated = new ButtonMicColor(GuiTools.getColorStateList(context,R.color.white), GuiTools.getColorStateList(context,R.color.gray));
        colorMutedDeactivated = new ButtonMicColor(GuiTools.getColorStateList(context,R.color.very_very_dark_gray), GuiTools.getColorStateList(context,R.color.very_very_light_gray));
    }

    public ButtonMic(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        colorActivated = new ButtonMicColor(GuiTools.getColorStateList(context,R.color.white), GuiTools.getColorStateList(context,R.color.primary));
        colorMutedActivated = new ButtonMicColor(GuiTools.getColorStateList(context,R.color.primary_very_dark), GuiTools.getColorStateList(context,R.color.primary_very_lite));
        colorDeactivated = new ButtonMicColor(GuiTools.getColorStateList(context,R.color.white), GuiTools.getColorStateList(context,R.color.gray));
        colorMutedDeactivated = new ButtonMicColor(GuiTools.getColorStateList(context,R.color.very_very_dark_gray), GuiTools.getColorStateList(context,R.color.very_very_light_gray));
    }

    public ButtonMic(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        colorActivated = new ButtonMicColor(GuiTools.getColorStateList(context,R.color.white), GuiTools.getColorStateList(context,R.color.primary));
        colorMutedActivated = new ButtonMicColor(GuiTools.getColorStateList(context,R.color.primary_very_dark), GuiTools.getColorStateList(context,R.color.primary_very_lite));
        colorDeactivated = new ButtonMicColor(GuiTools.getColorStateList(context,R.color.white), GuiTools.getColorStateList(context,R.color.gray));
        colorMutedDeactivated = new ButtonMicColor(GuiTools.getColorStateList(context,R.color.very_very_dark_gray), GuiTools.getColorStateList(context,R.color.very_very_light_gray));
    }

    public void deleteEditText(VoiceTranslationActivity activity, final ConversationMainFragment fragment, final ButtonKeyboard buttonKeyboard, final EditText editText) {
        animator.animateDeleteEditText(activity, this, buttonKeyboard, editText, new CustomAnimator.Listener() {
            @Override
            public void onAnimationStart() {
                fragment.setInputActive(false);
                buttonKeyboard.setClickable(false);
            }

            @Override
            public void onAnimationEnd() {
                fragment.setInputActive(true);
                buttonKeyboard.setClickable(true);
            }
        });
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        int oldState = this.state;
        this.state = state;

        if (state == STATE_NORMAL) {
            if (oldState == STATE_RETURN) {
                if (!isMute && activationStatus == ACTIVATED && fragment != null) {
                    fragment.startMicrophone(false);
                }
                if (micInput != null) {
                    // micInput appearance animation (TextView under mic) and microphone enlargement
                    animator.animateIconToMic(context, this, micInput);
                } else {
                    // microphone enlargement animation
                    animator.animateIconToMic(context, this);
                }
            } else if (oldState == STATE_SEND) {
                // in this case first we switch to the microphone icon with the animation and then we start the animation to delete the editText
                if (!isMute && activationStatus == ACTIVATED && fragment != null) {
                    fragment.startMicrophone(false);
                }
                editText.setText(""); // do it without activating the listener
                if (micInput != null) {
                    // animation micInput (TextView under the mic) and enlargement of the microphone after the change of icon from send to mic
                    if (isMute) {
                        animator.animateIconToMicAndIconChange(context, this, micInput, getDrawable(R.drawable.mic_mute));
                    }else{
                        animator.animateIconToMicAndIconChange(context, this, micInput, getDrawable(R.drawable.mic));
                    }
                } else {
                    // microphone enlargement animation
                    if(isMute){
                        animator.animateIconToMicAndIconChange(context, this, getDrawable(R.drawable.mic_mute));
                    }else {
                        animator.animateIconToMicAndIconChange(context, this, getDrawable(R.drawable.mic));
                    }
                }
            }
        } else if (state == STATE_RETURN) {
            if (oldState == STATE_NORMAL) {
                if(fragment != null) {
                    fragment.stopMicrophone(false);
                }
                if (micInput != null) {
                    // micInput appearance animation (TextView under mic) and microphone enlargement
                    animator.animateMicToIcon(context, this, micInput);
                } else {
                    // microphone enlargement animation
                    animator.animateMicToIcon(context, this);
                }

            } else if (oldState == STATE_SEND) {
                // change icon animation
                if(isMute){
                    animator.animateIconChange(this, getDrawable(R.drawable.mic_mute));
                }else{
                    animator.animateIconChange(this, getDrawable(R.drawable.mic));
                }
            }
        } else if (state == STATE_SEND) {
            // change icon animation
            animator.animateIconChange(this, getDrawable(R.drawable.send_icon));
        }
    }

    // to be set at the beginning only if micInput is present in the GUI
    public void setMicInput(TextView micInput) {
        this.micInput = micInput;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        boolean oldMute = isMute;
        isMute = mute;
        if (state == STATE_NORMAL) {
            if (mute) {
                animator.animateMute(context, this);
                currentColor = colorMutedActivated;  //da fare: controllare se questo codice viene eseguito solo quando il buttonMic è attivo
            } else {
                animator.animateUnmute(context, this);
                currentColor = colorActivated;  //da fare: controllare se questo codice viene eseguito solo quando il buttonMic è attivo
            }
        }
    }

    public void onVoiceStarted() {
        if (!isMute && activationStatus == ACTIVATED) {  // see if it makes sense to keep this check
            isListening = true;
            animator.animateOnVoiceStart(context,this);
        }
    }

    public void onVoiceEnded() {
        if (!isMute) {   // see if it makes sense to keep this check
            isListening = false;
            animator.animateOnVoiceEnd(context, this);
        }
    }

    @Override
    public void activate(boolean start) {
        super.activate(start);
        animator.animateActivation(context, this);
        currentColor = isMute ? colorMutedActivated : colorActivated;
        if (start && fragment != null) {
            fragment.startMicrophone(false);
        }
    }

    @Override
    public void deactivate(int reason) {
        super.deactivate(reason);
        switch (reason) {
            case DEACTIVATED_FOR_MISSING_MIC_PERMISSION:
                //setImageDrawable(getDrawable(R.drawable.mic));
                animator.animateDeactivation(context, this);
                currentColor = isMute ? colorMutedDeactivated : colorDeactivated;
                if(fragment != null) {
                    fragment.stopMicrophone(false);
                }
                break;
            case DEACTIVATED:
                if (currentColor == null) {  //for differentiating the deactivate at the start of WalkieTalkie mode (color == null) from the one caused by programmatic stop of mic
                    currentColor = isMute ? colorMutedDeactivated : colorDeactivated;
                    //setImageDrawable(getDrawable(R.drawable.mic));
                } else {
                    //setImageDrawable(getDrawable(R.drawable.mic));
                    animator.animateDeactivation(context, this);
                    currentColor = isMute ? colorMutedDeactivated : colorDeactivated;
                }
                break;
        }
    }

    public void setFragment(@Nullable MicrophoneComunicable fragment) {
        this.fragment = fragment;
    }

    public Drawable getDrawable(int id) {
        Drawable drawable = getResources().getDrawable(id, null);
        drawable.setTintList(currentColor.iconColor);
        return drawable;
    }

    public ButtonMicColor getCurrentColor(){
        return currentColor;
    }

    public boolean isListening() {
        return isListening;
    }


    public static class ButtonMicColor {
        public ColorStateList iconColor;
        public ColorStateList backgroundColor;

        public ButtonMicColor(ColorStateList iconColor, ColorStateList backgroundColor){
            this.iconColor = iconColor;
            this.backgroundColor = backgroundColor;
        }
    }
}
