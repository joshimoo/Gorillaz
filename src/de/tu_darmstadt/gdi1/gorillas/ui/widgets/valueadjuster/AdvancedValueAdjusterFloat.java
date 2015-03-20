/*
 * Copyright (c) 2008-2010, Matthias Mann
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Matthias Mann nor the names of its contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.tu_darmstadt.gdi1.gorillas.ui.widgets.valueadjuster;

import com.sun.xml.internal.fastinfoset.util.CharArray;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.model.FloatModel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * This is a slightly changed version of Matthias Mann's {@link de.matthiasmann.twl.ValueAdjusterInt}
 * class.
 * <p>
 * It just extends {@link de.tu_darmstadt.gdi1.gorillas.ui.widgets.valueadjuster.AdvancedValueAdjuster} instead of {@link de.matthiasmann.twl.ValueAdjuster}
 * and overrides {@link .handleEditCallback(int key)}. This method's body makes
 * sure that nothing but an int bigger or equal than <code>minValue</code> and smaller
 * or equal than <code>maxValue</code> is accepted.
 *
 * @author Matthias Mann, Peter Kl�ckner
 */
public class AdvancedValueAdjusterFloat extends AdvancedValueAdjuster {

    private float value;
    private float minValue;
    private float maxValue = 100;
    private float dragStartValue;
    private FloatModel model;
    private Runnable modelCallback;

    public AdvancedValueAdjusterFloat() {
        setTheme("valueadjuster");
        setDisplayText();
    }

    public AdvancedValueAdjusterFloat(FloatModel model) {
        setTheme("valueadjuster");
        setModel(model);
    }

    public float getMaxValue() {
        if (model != null) {
            maxValue = model.getMaxValue();
        }
        return maxValue;
    }

    public float getMinValue() {
        if (model != null) {
            minValue = model.getMinValue();
        }
        return minValue;
    }

    public void setMinMaxValue(float minValue, float maxValue) {
        if (maxValue < minValue) {
            throw new IllegalArgumentException("maxValue < minValue");
        }
        this.minValue = minValue;
        this.maxValue = maxValue;
        setValue(value);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        value = Math.max(getMinValue(), Math.min(getMaxValue(), value));
        if (this.value != value) {
            this.value = value;
            if (model != null) {
                model.setValue(value);
            }
            setDisplayText();
        }
    }

    public FloatModel getModel() {
        return model;
    }

    public void setModel(FloatModel model) {
        if (this.model != model) {
            removeModelCallback();
            this.model = model;
            if (model != null) {
                this.minValue = model.getMinValue();
                this.maxValue = model.getMaxValue();
                addModelCallback();
            }
        }
    }

    @Override
    protected String onEditStart() {
        return formatText();
    }

    @Override
    protected boolean onEditEnd(String text) {
        if (getIsDotValid(text)) try {
            setValue(Float.parseFloat(text));
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
        else return false;
    }

    @Override
    protected String validateEdit(String text) {
        try {
            Float.parseFloat(text);
            return null;
        } catch (NumberFormatException ex) {
            return ex.toString();
        }
    }

    @Override
    protected void onEditCanceled() {
    }

    @Override
    protected boolean shouldStartEdit(char ch) {
        return (ch >= '0' && ch <= '9') || (ch == '-');
    }

    @Override
    protected void onDragStart() {
        dragStartValue = value;
    }

    @Override
    protected void onDragUpdate(int dragDelta) {
        float range = Math.max(1, Math.abs(getMaxValue() - getMinValue()));
        setValue(dragStartValue + dragDelta / Math.max(3, getWidth() / range));
    }

    @Override
    protected void onDragCancelled() {
        setValue(dragStartValue);
    }

    @Override
    protected void doDecrement() {
        setValue(value - 1);
    }

    @Override
    protected void doIncrement() {
        setValue(value + 1);
    }

    @Override
    protected String formatText() {
        return Float.toString(value);
    }

    protected void syncWithModel() {
        cancelEdit();
        this.minValue = model.getMinValue();
        this.maxValue = model.getMaxValue();
        this.value = model.getValue();
        setDisplayText();
    }

    @Override
    protected void afterAddToGUI(GUI gui) {
        super.afterAddToGUI(gui);
        addModelCallback();
    }

    @Override
    protected void beforeRemoveFromGUI(GUI gui) {
        removeModelCallback();
        super.beforeRemoveFromGUI(gui);
    }

    protected void removeModelCallback() {
        if (model != null && modelCallback != null) {
            model.removeCallback(modelCallback);
        }
    }

    protected void addModelCallback() {
        if (model != null && getGUI() != null) {
            if (modelCallback == null) {
                modelCallback = new ModelCallback();
            }
            model.addCallback(modelCallback);
            syncWithModel();
        }
    }

    protected void handleEditCallback(int key) {

        switch (key) {
            case Event.KEY_RETURN:
                if (onEditEnd(editField.getText())) {
                    label.setVisible(true);
                    editField.setVisible(false);
                }
                break;

            case Event.KEY_ESCAPE:
                cancelEdit();
                break;

            case Event.KEY_BACKSLASH:
                String valueEditField = editField.getText();
                editField.setText(valueEditField.substring(0, valueEditField.length() - 2));
                break;

            case 0:
                String inputText = editField.getText();

                if (inputText.isEmpty()) {
                    return;
                }

                char inputChar = inputText.charAt(inputText.length() - 1);

                boolean dotOK = ((Character.compare(inputChar, '.') == 0) && !isDotInsert(inputText) && (inputText.length() > 0));

                boolean numberOrPoint = Character.isDigit(inputChar) || dotOK;

                if (!numberOrPoint || (Float.parseFloat(inputText) > maxValue || Float.parseFloat(inputText) < minValue)) {

                    // a call of setText on an EditField triggers the callback, so
                    // remove callback before and add it again after the call
                    // editField.removeCallback(callback);
                    // Checks if
                    if ((Character.compare(inputChar, '.') == 0)) {
                        editField.setText(inputText.substring(0, inputText.length() - 1));
                    }
                    // Set to max if the entered number is to big
                    else if (numberOrPoint && Float.parseFloat(inputText) > maxValue) {
                        editField.setText(Float.toString(getMaxValue()));
                    }
                    // Set to min if the entered number is to small
                    else if (numberOrPoint && Float.parseFloat(inputText) < minValue) {
                        editField.setText(Float.toString(getMinValue()));
                    }
                    else {
                        editField.setText(inputText.substring(0, inputText.length() - 1));
                    }
                    // editField.addCallback(callback);
                }

            default:
                // editField.setErrorMessage(validateEdit(editField.getText()));
        }
    }

    public boolean getIsDotValid(String inputText) {
        return (Character.compare(inputText.charAt(0), '.') != 0) && (Character.compare(inputText.charAt(inputText.length() - 1), '.') != 0);
    }

    public boolean isDotInsert(String text) {
        if(text.substring(0,text.length()-2).contains(new dotC())){
            return true;
        }
        else {
            return false;
        }
    }

    class dotC implements CharSequence
    {
        @Override public int length () {
            return 1;
        }

        @Override public char charAt ( int index){
             return '.';
        }
        public dotC()
        {

        }

        @Override public CharSequence subSequence ( int start, int end) {
            return new dotC();
        }
    }
}
