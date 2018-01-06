package org.ocaml.ide.settings

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.EnumComboBoxModel
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.*
import com.intellij.uiDesigner.core.GridLayoutManager
import java.awt.Dimension
import java.awt.Insets
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class OCamlPluginConfigurable : SearchableConfigurable {

    val config = OCamlPluginConfig.instance
    private val externalFmtCombo = ComboBox<ExternalFormatter>(EnumComboBoxModel<ExternalFormatter>(ExternalFormatter::class.java))
    private val enableFormatter = JCheckBox()

    override fun getId(): String {
        return "preference.OCamlPluginConfigurable"
    }

    override fun getDisplayName(): String {
        return "OCaml"
    }

    override fun isModified(): Boolean {
        return config.externalFormatter != externalFmtCombo.selectedItem
                || config.externalFormatterEnabled != enableFormatter.isSelected
    }

    override fun apply() {
        config.externalFormatter = externalFmtCombo.selectedItem as ExternalFormatter
        config.externalFormatterEnabled = enableFormatter.isSelected
    }

    fun initState() {
        externalFmtCombo.selectedItem = config.externalFormatter
        enableFormatter.isSelected = config.externalFormatterEnabled
    }

    override fun createComponent(): JComponent? {
        val rootPanel = JPanel()
        rootPanel.layout = GridLayoutManager(3, 3, Insets(0, 0, 0, 0), -1, -1)
        rootPanel.isRequestFocusEnabled = true

        var gridConstraints = GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, Dimension(80, 16), null, null, 0)
        rootPanel.add(enableFormatter, gridConstraints)

        val label1 = JLabel()
        label1.text = "External code formatter"
        val hSizePol = SIZEPOLICY_CAN_GROW or SIZEPOLICY_CAN_SHRINK
        val vSizePol = SIZEPOLICY_CAN_GROW or SIZEPOLICY_CAN_SHRINK
        gridConstraints = GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, hSizePol, vSizePol, Dimension(80, 16), null, null, 0)
        rootPanel.add(label1, gridConstraints)

        gridConstraints = GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, hSizePol, vSizePol, Dimension(80, 16), null, null, 0)
        rootPanel.add(externalFmtCombo, gridConstraints)

        initState()
        return rootPanel
    }
}