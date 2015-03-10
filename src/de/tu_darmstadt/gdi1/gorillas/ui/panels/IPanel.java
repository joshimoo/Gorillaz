package de.tu_darmstadt.gdi1.gorillas.ui.panels;

import de.matthiasmann.twl.slick.RootPane;

/**
 * Defines a Standard set of Methods, that all Panels must have
 */
public interface IPanel {
    abstract RootPane addToRootPane(RootPane rp);
    abstract void layoutPanel();
    abstract void enablePanel(Boolean enable);
}
