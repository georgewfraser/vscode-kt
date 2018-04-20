package org.javacs.kt

import org.hamcrest.Matchers.hasItem
import org.junit.Assert.assertThat
import org.junit.Test

class CompletionsTest: LanguageServerTestFixture("completions") {

    @Test fun `complete instance member`() {
        val file = "InstanceMember.kt"
        open(file)

        val completions = languageServer.textDocumentService.completion(textDocumentPosition(file, 3, 15)).get().right!!
        val labels = completions.items.map { it.label }

        assertThat(labels, hasItem("instanceFoo"))
    }

    @Test fun `complete object member`() {
        val file = "ObjectMember.kt"
        open(file)

        val completions = languageServer.textDocumentService.completion(textDocumentPosition(file, 2, 17)).get().right!!
        val labels = completions.items.map { it.label }

        assertThat(labels, hasItem("objectFoo"))
    }

    @Test fun `complete identifiers in function scope`() {
        val file = "FunctionScope.kt"
        open(file)

        val completions = languageServer.textDocumentService.completion(textDocumentPosition(file, 4, 10)).get().right!!
        val labels = completions.items.map { it.label }

        assertThat(labels, hasItem("anArgument"))
        assertThat(labels, hasItem("aLocal"))
        assertThat(labels, hasItem("aClassVal"))
        assertThat(labels, hasItem("aClassFun"))
        assertThat(labels, hasItem("aCompanionVal"))
        assertThat(labels, hasItem("aCompanionFun"))
    }

    @Test fun `complete a type name`() {
        val file = "Types.kt"
        open(file)

        val completions = languageServer.textDocumentService.completion(textDocumentPosition(file, 2, 25)).get().right!!
        val labels = completions.items.map { it.label }

        assertThat(labels, hasItem("SomeInnerClass"))
        assertThat(labels, hasItem("String"))
        assertThat(labels, hasItem("SomeInnerObject"))
        assertThat(labels, hasItem("SomeAlias"))
    }

    @Test fun `fill an empty body`() {
        val file = "FillEmptyBody.kt"
        open(file)

        replace(file, 2, 16, "", """"
            Callee.
""")
        val completions = languageServer.textDocumentService.completion(textDocumentPosition(file, 3, 20)).get().right!!
        val labels = completions.items.map { it.label }

        assertThat(labels, hasItem("bar"))
    }

    @Test fun `complete a constructor`() {
        val file = "Constructor.kt"
        open(file)

        val completions = languageServer.textDocumentService.completion(textDocumentPosition(file, 2, 10)).get().right!!
        val labels = completions.items.map { it.label }

        assertThat(labels, hasItem("SomeConstructor"))
    }

    @Test fun `complete in the middle of a function`() {
        val file = "MiddleOfFunction.kt"
        open(file)

        val completions = languageServer.textDocumentService.completion(textDocumentPosition(file, 3, 11)).get().right!!
        val labels = completions.items.map { it.label }

        assertThat(labels, hasItem("subSequence"))
    }

    @Test fun `complete with backquotes`() {
        val file = "BackquotedFunction.kt"
        open(file)

        val completions = languageServer.textDocumentService.completion(textDocumentPosition(file, 2, 7)).get().right!!
        val insertText = completions.items.map { it.insertText }

        assertThat(insertText, hasItem("`fun that needs backquotes`()"))
    }
}