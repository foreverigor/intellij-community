package com.intellij.documentation.mdn

import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlToken
import com.intellij.xml.util.HtmlUtil
import com.intellij.xml.util.XmlUtil

class XmlMdnDocumentationProvider : DocumentationProvider {

  override fun getUrlFor(element: PsiElement, originalElement: PsiElement?): List<String>? =
    getMdnDocumentation(element, originalElement)?.let { listOf(it.url) }

  override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String? =
    generateDoc(element, originalElement, true)

  override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? =
    generateDoc(element, originalElement, false)

  companion object {

    private val supportedNamespaces = setOf(HtmlUtil.SVG_NAMESPACE, HtmlUtil.MATH_ML_NAMESPACE, XmlUtil.HTML_URI, XmlUtil.XHTML_URI)

    private fun generateDoc(element: PsiElement, originalElement: PsiElement?, quickDoc: Boolean): String? =
      getMdnDocumentation(element, originalElement)?.getDocumentation(true, quickDoc)

    private fun getMdnDocumentation(element: PsiElement, originalElement: PsiElement?): MdnSymbolDocumentation? =
      originalElement.takeIf { it is XmlElement || it is XmlToken }
        ?.let { PsiTreeUtil.getNonStrictParentOfType<XmlElement>(it, XmlTag::class.java, XmlAttribute::class.java) }
        ?.let {
          when {
            it is XmlAttribute && supportedNamespaces.contains(it.parent.getNamespaceByPrefix(it.namespacePrefix)) ->
              getHtmlMdnDocumentation(element, it.parent)
            it is XmlTag && supportedNamespaces.contains(it.namespace) -> getHtmlMdnDocumentation(element, it)
            else -> null
          }
        }
  }
}