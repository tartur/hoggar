package org.hoggar.lang.parser.psi

import com.intellij.psi.tree.IElementType
import org.hoggar.lang.OcamlLanguage

/**
 * Created by sidharthkuruvila on 10/05/16.
 */
class OcamlCompositeElementType(s: String) : IElementType(s, OcamlLanguage.INSTANCE)