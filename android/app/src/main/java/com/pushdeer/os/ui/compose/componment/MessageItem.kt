package com.pushdeer.os.ui.compose.componment

import android.text.TextUtils
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.load
import com.pushdeer.os.R
import com.pushdeer.os.data.database.entity.MessageEntity
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.util.CurrentTimeUtil
import com.pushdeer.os.values.ConstValues


@ExperimentalMaterialApi
@Composable
fun PlainTextMessageItem(message: MessageEntity, requestHolder: RequestHolder) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                requestHolder.clip.copyMessagePlainText(message.text)
            }
            .background(color = MaterialTheme.colors.surface)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_deer_head_with_mail),
                contentDescription = "",
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "${message.pushkey_name}·${
                    CurrentTimeUtil.resolveUTCTimeAndNow(
                        message.created_at,
                        System.currentTimeMillis()
                    )
                }"
            )
        }

        CardItemWithContent {
            Column(modifier = Modifier.fillMaxSize()) {

                if (TextUtils.isEmpty(message.desp)) {
                    Text(
                        text = message.text,
                        overflow = TextOverflow.Visible,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                } else {
                    Text(
                        text = message.text,
                        overflow = TextOverflow.Visible,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 4.dp)
                            .alpha(0.8F)
                    )
                    Text(
                        text = message.desp,
                        overflow = TextOverflow.Visible,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp)
                            .alpha(0.5F)
                    )
                }

            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ImageMessageItem(message: MessageEntity, requestHolder: RequestHolder) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                requestHolder.clip.copyMessagePlainText(message.text)
            }
            .background(color = MaterialTheme.colors.surface)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ConstValues.MainPageSidePadding)
                .padding(bottom = 12.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_deer_head_with_mail),
                contentDescription = "",
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "${message.pushkey_name}·${
                    CurrentTimeUtil.resolveUTCTimeAndNow(
                        message.created_at,
                        System.currentTimeMillis()
                    )
                }"
            )
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            AndroidView(
                factory = {
                    ImageView(it).apply {
                        scaleType = ImageView.ScaleType.FIT_CENTER
                    }
                },
                update = { view ->
                    view.load(message.text, requestHolder.coilImageLoader)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MarkdownMessageItem(message: MessageEntity, requestHolder: RequestHolder) {

//    val a = "![logo](./art/markwon_logo.png)\n" +
//            "\n" +
//            "# Markwon\n" +
//            "\n" +
//            "[![Build](https://github.com/noties/Markwon/workflows/Build/badge.svg)](https://github.com/noties/Markwon/actions)\n" +
//            "\n" +
//            "**Markwon** is a markdown library for Android. It parses markdown\n" +
//            "following [commonmark-spec] with the help of amazing [commonmark-java]\n" +
//            "library and renders result as _Android-native_ Spannables. **No HTML**\n" +
//            "is involved as an intermediate step. <u>**No WebView** is required</u>.\n" +
//            "It's extremely fast, feature-rich and extensible.\n" +
//            "\n" +
//            "It gives ability to display markdown in all TextView widgets\n" +
//            "(**TextView**, **Button**, **Switch**, **CheckBox**, etc), **Toasts**\n" +
//            "and all other places that accept **Spanned content**. Library provides\n" +
//            "reasonable defaults to display style of a markdown content but also \n" +
//            "gives all the means to tweak the appearance if desired. All markdown\n" +
//            "features listed in [commonmark-spec] are supported\n" +
//            "(including support for **inlined/block HTML code**, **markdown tables**,\n" +
//            "**images** and **syntax highlight**).\n" +
//            "\n" +
//            "`Markwon` comes with a [sample application](./app-sample/). It is a\n" +
//            "collection of library usages that comes with search and source code for\n" +
//            "each code sample.\n" +
//            "\n" +
//            "Since version **4.2.0** **Markwon** comes with an [editor](./markwon-editor/) to _highlight_ markdown input\n" +
//            "as user types (for example in **EditText**).\n" +
//            "\n" +
//            "[commonmark-spec]: https://spec.commonmark.org/0.28/\n" +
//            "[commonmark-java]: https://github.com/atlassian/commonmark-java/blob/master/README.md\n" +
//            "\n" +
//            "## Installation\n" +
//            "\n" +
//            "![stable](https://img.shields.io/maven-central/v/io.noties.markwon/core.svg?label=stable)\n" +
//            "![snapshot](https://img.shields.io/nexus/s/https/oss.sonatype.org/io.noties.markwon/core.svg?label=snapshot)\n" +
//            "\n" +
//            "```kotlin\n" +
//            "implementation \"io.noties.markwon:core:\"\n" +
//            "```\n" +
//            "\n" +
//            "Full list of available artifacts is present in the [install section](https://noties.github.io/Markwon/docs/v4/install.html)\n" +
//            "of the [documentation] web-site.\n" +
//            "\n" +
//            "Please visit [documentation] web-site for further reference.\n" +
//            "\n" +
//            "\n" +
//            "> You can find previous version of Markwon in [2.x.x](https://github.com/noties/Markwon/tree/2.x.x)\n" +
//            "and [3.x.x](https://github.com/noties/Markwon/tree/3.x.x) branches\n" +
//            "\n" +
//            "## Supported markdown features:\n" +
//            "* Emphasis (`*`, `_`)\n" +
//            "* Strong emphasis (`**`, `__`)\n" +
//            "* Strike-through (`~~`)\n" +
//            "* Headers (`#{1,6}`)\n" +
//            "* Links (`[]()` && `[][]`)\n" +
//            "* Images\n" +
//            "* Thematic break (`---`, `***`, `___`)\n" +
//            "* Quotes & nested quotes (`>{1,}`)\n" +
//            "* Ordered & non-ordered lists & nested ones\n" +
//            "* Inline code\n" +
//            "* Code blocks\n" +
//            "* Tables (*with limitations*)\n" +
//            "* Syntax highlight\n" +
//            "* LaTeX formulas\n" +
//            "* HTML\n" +
//            "  * Emphasis (`<i>`, `<em>`, `<cite>`, `<dfn>`)\n" +
//            "  * Strong emphasis (`<b>`, `<strong>`)\n" +
//            "  * SuperScript (`<sup>`)\n" +
//            "  * SubScript (`<sub>`)\n" +
//            "  * Underline (`<u>`, `ins`)\n" +
//            "  * Strike-through (`<s>`, `<strike>`, `<del>`)\n" +
//            "  * Link (`a`)\n" +
//            "  * Lists (`ul`, `ol`)\n" +
//            "  * Images (`img` will require configured image loader)\n" +
//            "  * Blockquote (`blockquote`)\n" +
//            "  * Heading (`h1`, `h2`, `h3`, `h4`, `h5`, `h6`)\n" +
//            "  * there is support to render any HTML tag\n" +
//            "* Task lists:\n" +
//            "- [ ] Not _done_\n" +
//            "  - [X] **Done** with `X`\n" +
//            "  - [x] ~~and~~ **or** small `x`\n" +
//            "---\n" +
//            "\n" +
//            "## Screenshots\n" +
//            "\n" +
//            "Taken with default configuration (except for image loading) in [sample app](./app-sample/):\n" +
//            "\n" +
//            "<a href=\"./art/mw_light_01.png\"><img src=\"./art/mw_light_01.png\" width=\"30%\" /></a>\n" +
//            "<a href=\"./art/mw_light_02.png\"><img src=\"./art/mw_light_02.png\" width=\"30%\" /></a>\n" +
//            "<a href=\"./art/mw_light_03.png\"><img src=\"./art/mw_light_03.png\" width=\"30%\" /></a>\n" +
//            "<a href=\"./art/mw_dark_01.png\"><img src=\"./art/mw_dark_01.png\" width=\"30%\" /></a>\n" +
//            "\n" +
//            "By default configuration uses TextView textColor for styling, so changing textColor changes style\n" +
//            "\n" +
//            "---\n" +
//            "\n" +
//            "## Documentation\n" +
//            "\n" +
//            "Please visit [documentation] web-site for reference\n" +
//            "\n" +
//            "[documentation]: https://noties.github.io/Markwon\n" +
//            "\n" +
//            "\n" +
//            "## Consulting\n" +
//            "Paid consulting is available. Please reach me out at [markwon+consulting[at]noties.io](mailto:markwon+consulting@noties.io)\n" +
//            "to discuss your idea or a project\n" +
//            "\n" +
//            "---\n" +
//            "\n" +
//            "# Demo\n" +
//            "Based on [this cheatsheet][cheatsheet]\n" +
//            "\n" +
//            "---\n" +
//            "\n" +
//            "## Headers\n" +
//            "---\n" +
//            "# Header 1\n" +
//            "## Header 2\n" +
//            "### Header 3\n" +
//            "#### Header 4\n" +
//            "##### Header 5\n" +
//            "###### Header 6\n" +
//            "---\n" +
//            "\n" +
//            "## Emphasis\n" +
//            "\n" +
//            "Emphasis, aka italics, with *asterisks* or _underscores_.\n" +
//            "\n" +
//            "Strong emphasis, aka bold, with **asterisks** or __underscores__.\n" +
//            "\n" +
//            "Combined emphasis with **asterisks and _underscores_**.\n" +
//            "\n" +
//            "Strikethrough uses two tildes. ~~Scratch this.~~\n" +
//            "\n" +
//            "---\n" +
//            "\n" +
//            "## Lists\n" +
//            "1. First ordered list item\n" +
//            "2. Another item\n" +
//            "  * Unordered sub-list.\n" +
//            "1. Actual numbers don't matter, just that it's a number\n" +
//            "  1. Ordered sub-list\n" +
//            "4. And another item.\n" +
//            "\n" +
//            "   You can have properly indented paragraphs within list items. Notice the blank line above, and the leading spaces (at least one, but we'll use three here to also align the raw Markdown).\n" +
//            "\n" +
//            "   To have a line break without a paragraph, you will need to use two trailing spaces.\n" +
//            "   Note that this line is separate, but within the same paragraph.\n" +
//            "   (This is contrary to the typical GFM line break behaviour, where trailing spaces are not required.)\n" +
//            "\n" +
//            "* Unordered list can use asterisks\n" +
//            "- Or minuses\n" +
//            "+ Or pluses\n" +
//            "\n" +
//            "---\n" +
//            "\n" +
//            "## Links\n" +
//            "\n" +
//            "[I'm an inline-style link](https://www.google.com)\n" +
//            "\n" +
//            "[I'm a reference-style link][Arbitrary case-insensitive reference text]\n" +
//            "\n" +
//            "[I'm a relative reference to a repository file](../blob/master/LICENSE)\n" +
//            "\n" +
//            "[You can use numbers for reference-style link definitions][1]\n" +
//            "\n" +
//            "Or leave it empty and use the [link text itself].\n" +
//            "\n" +
//            "---\n" +
//            "\n" +
//            "## Code\n" +
//            "\n" +
//            "Inline `code` has `back-ticks around` it.\n" +
//            "\n" +
//            "```javascript\n" +
//            "var s = \"JavaScript syntax highlighting\";\n" +
//            "alert(s);\n" +
//            "```\n" +
//            "\n" +
//            "```python\n" +
//            "s = \"Python syntax highlighting\"\n" +
//            "print s\n" +
//            "```\n" +
//            "\n" +
//            "```java\n" +
//            "/**\n" +
//            " * Helper method to obtain a Parser with registered strike-through &amp; table extensions\n" +
//            " * &amp; task lists (added in 1.0.1)\n" +
//            " *\n" +
//            " * @return a Parser instance that is supported by this library\n" +
//            " * @since 1.0.0\n" +
//            " */\n" +
//            "@NonNull\n" +
//            "public static Parser createParser() {\n" +
//            "  return new Parser.Builder()\n" +
//            "      .extensions(Arrays.asList(\n" +
//            "          StrikethroughExtension.create(),\n" +
//            "          TablesExtension.create(),\n" +
//            "          TaskListExtension.create()\n" +
//            "      ))\n" +
//            "      .build();\n" +
//            "}\n" +
//            "```\n" +
//            "\n" +
//            "```xml\n" +
//            "<ScrollView\n" +
//            "  android:id=\"@+id/scroll_view\"\n" +
//            "  android:layout_width=\"match_parent\"\n" +
//            "  android:layout_height=\"match_parent\"\n" +
//            "  android:layout_marginTop=\"?android:attr/actionBarSize\">\n" +
//            "\n" +
//            "  <TextView\n" +
//            "    android:id=\"@+id/text\"\n" +
//            "    android:layout_width=\"match_parent\"\n" +
//            "    android:layout_height=\"wrap_content\"\n" +
//            "    android:layout_margin=\"16dip\"\n" +
//            "    android:lineSpacingExtra=\"2dip\"\n" +
//            "    android:textSize=\"16sp\"\n" +
//            "    tools:text=\"yo\\nman\" />\n" +
//            "\n" +
//            "</ScrollView>\n" +
//            "```\n"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                requestHolder.clip.copyMessagePlainText(message.text)
            }
            .background(color = MaterialTheme.colors.surface)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .height(IntrinsicSize.Max)
                .padding(bottom = 12.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.ic_deer_head_with_mail),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp)
                )
            }

            Text(
                text = "${message.pushkey_name}·${
                    CurrentTimeUtil.resolveUTCTimeAndNow(
                        message.created_at,
                        System.currentTimeMillis()
                    )
                }"
            )
        }

        CardItemWithContent {
            AndroidView(
                factory = { ctx ->
                    android.widget.TextView(ctx)
                },
                update = { view ->
                    view.apply {
                        this.post {
                            requestHolder.markdown.setMarkdown(
                                this,
                                "${message.text}\n\n${message.desp}"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}