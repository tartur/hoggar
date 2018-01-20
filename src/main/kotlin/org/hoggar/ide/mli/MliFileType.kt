package org.hoggar.ide.mli

import com.intellij.openapi.fileTypes.LanguageFileType
import org.hoggar.lang.OcamlIcons
import javax.swing.Icon

/**
 * Created by sidharthkuruvila on 17/05/16.
 */
object MliFileType : LanguageFileType(OcamlInterfaceLanguage) {

    override fun getDescription(): String {
        return "Ocaml interface file"
    }

    override fun getDefaultExtension(): String {
        return "mli"
    }


    override fun getName(): String {
        return "Ocaml Interface File"
    }

    override fun getIcon(): Icon? {
        return OcamlIcons.INTERFACE_ICON_FILE
    }

}