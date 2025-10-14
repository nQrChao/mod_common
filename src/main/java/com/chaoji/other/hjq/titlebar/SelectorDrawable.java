package com.chaoji.other.hjq.titlebar;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;


@SuppressWarnings("unused")
public final class SelectorDrawable extends StateListDrawable {

    public final static class Builder {

        private Drawable mDefault;

        private Drawable mFocused;

        private Drawable mPressed;

        private Drawable mChecked;

        private Drawable mEnabled;

        private Drawable mSelected;

        private Drawable mHovered;

        public Builder setDefault(Drawable drawable) {
            mDefault = drawable;
            return this;
        }

        public Builder setFocused(Drawable drawable) {
            mFocused = drawable;
            return this;
        }

        public Builder setPressed(Drawable drawable) {
            mPressed = drawable;
            return this;
        }

        public Builder setChecked(Drawable drawable) {
            mChecked = drawable;
            return this;
        }

        public Builder setEnabled(Drawable drawable) {
            mEnabled = drawable;
            return this;
        }

        public Builder setSelected(Drawable drawable) {
            mSelected = drawable;
            return this;
        }

        public Builder setHovered(Drawable drawable) {
            mHovered = drawable;
            return this;
        }

        public SelectorDrawable build() {
            SelectorDrawable selector = new SelectorDrawable();
            if (mPressed != null) {
                selector.addState(new int[]{android.R.attr.state_pressed}, mPressed);
            }
            if (mFocused != null) {
                selector.addState(new int[]{android.R.attr.state_focused}, mFocused);
            }
            if (mChecked != null) {
                selector.addState(new int[]{android.R.attr.state_checked}, mChecked);
            }
            if (mEnabled != null) {
                selector.addState(new int[]{android.R.attr.state_enabled}, mEnabled);
            }
            if (mSelected != null) {
                selector.addState(new int[]{android.R.attr.state_selected}, mSelected);
            }
            if (mHovered != null) {
                selector.addState(new int[]{android.R.attr.state_hovered}, mHovered);
            }
            if (mDefault != null) {
                selector.addState(new int[]{}, mDefault);
            }
            return selector;
        }
    }
}