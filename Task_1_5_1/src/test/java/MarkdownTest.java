import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.markdown.Link;
import org.markdown.List;
import org.markdown.Table;
import org.markdown.Text;

/**
 * Название говорит само за себя.
 */
public class MarkdownTest {
    /**
     * Название говорит само за себя.
     */
    @Test
    void testImage() {
        Link.Builder builder = new Link.Builder();
        builder.isImage(true);
        builder.textAppend(new Text("this is the link"));
        builder.urlReplace("https://my_site.com");
        Link link = builder.build();
        Assertions.assertEquals("![this is the link](https://my_site.com)", link.toString());
    }

    /**
     * Название говорит само за себя.
     */
    @Test
    void testLink() {
        Link.Builder builder = new Link.Builder();
        builder.isImage(false);
        builder.textAppend(new Text("dddddddd"));
        builder.textRemove(0);
        builder.textAppend(new Text("HAHAHA "));
        builder.textAppend(new Text("this is the link"));
        builder.urlReplace("https://my_site.com");
        Link link = builder.build();
        Assertions.assertEquals("[HAHAHA this is the link](https://my_site.com)", link.toString());
    }

    /**
     * Название говорит само за себя.
     */
    @Test
    void testList() {
        List.Builder builder = new List.Builder();
        builder.add(new Text("IT'S"));
        builder.add(new Text("MY"));
        builder.add(new Text("DIE"));
        builder.remove(2);
        builder.add(new Text("LIFE"));
        List list = builder.build();
        Assertions.assertEquals("+ IT'S\n+ MY\n+ LIFE\n", list.toString());
    }

    /**
     * Название говорит само за себя.
     */
    @Test
    void testOrderedList() {
        List.Builder builder = new List.Builder();
        builder.setTypeOfList(List.Builder.ORDEREDLIST);
        builder.add(new Text("IT'S"));
        builder.add(new Text("MY"));
        builder.add(new Text("DIE"));
        builder.remove(2);
        builder.add(new Text("LIFE"));
        List list = builder.build();
        Assertions.assertEquals(list.toString(), "1 IT'S\n2 MY\n3 LIFE\n");
    }

    /**
     * Название говорит само за себя.
     */
    @Test
    void testTaskList() {
        List.Builder builder = new List.Builder();
        builder.setTypeOfList(List.Builder.TASKLIST);
        builder.add(new Text("IT'S"));
        builder.add(new Text("MY"));
        builder.add(new Text("DIE"));
        builder.remove(2);
        builder.add(new Text("LIFE"));
        builder.setVal(true, 2);
        List list = builder.build();
        Assertions.assertEquals("- [ ] IT'S\n- [ ] MY\n- [x] LIFE\n", list.toString());
    }

    /**
     * Название говорит само за себя.
     */
    @Test
    void testTable() {
        Table.Builder builder = new Table.Builder();
        builder.withCountOfColumn(3);
        builder.withAlignments(Table.ALIGN_CENTER, Table.ALIGN_RIGHT, Table.ALIGN_RIGHT);
        builder.addRow(new Text.Strikethrough("Header"), new Text.Code("Code"),
                new Text.Italic("Random"));
        builder.addRow(new Text.Strikethrough("Header"), new Text.Code("Code"),
                new Text.Italic("Random"));
        builder.addRow(new Text.Strikethrough("Header"), new Text.Code("Code"),
                new Text.Italic("Random"));
        Assertions.assertEquals("|~~Header~~|`Code`|*Random*|\n"
                + "|:-:|-:|-:|\n"
                + "|~~Header~~|`Code`|*Random*|\n"
                + "|~~Header~~|`Code`|*Random*|\n",
                builder.build().toString());

    }

    /**
     * Название говорит само за себя.
     */
    @Test
    void testText() {
        Text.Builder builder = new Text.Builder();
        builder.append("HEADING");
        builder.setType(Text.Builder.HEADING2);
        Assertions.assertEquals(builder.build().toString(), "## HEADING\n");

        builder.remove(0);
        builder.append("WRONG TEXT");
        builder.setType(Text.Builder.STRIKETHROUGH);
        Assertions.assertEquals(builder.build().toString(), "~~WRONG TEXT~~");

        builder.remove(0);
        builder.append("QUOTE");
        builder.setType(Text.Builder.QUOTE);
        Assertions.assertEquals(builder.build().toString(), ">QUOTE\n");

        builder.remove(0);
        builder.append("CODE");
        builder.setType(Text.Builder.CODEBLOCK);
        Assertions.assertEquals(builder.build().toString(), "```\n"
                + "CODE\n"
                + "```\n");

        builder.remove(0);
        builder.append("ITALIC");
        builder.setType(Text.Builder.ITALIC);
        Assertions.assertEquals(builder.build().toString(), "*ITALIC*");

        builder.remove(0);
        builder.append("BOLD");
        builder.setType(Text.Builder.BOLD);
        Assertions.assertEquals(builder.build().toString(), "**BOLD**");

        builder.remove(0);
        builder.append("CODE");
        builder.setType(Text.Builder.CODE);
        Assertions.assertEquals(builder.build().toString(), "`CODE`");
    }

    /**
     * Название говорит само за себя.
     */
    @Test
    void testEqualsTable() {
        Table.Builder builder = new Table.Builder();
        builder.withCountOfColumn(3);
        builder.withAlignments(Table.ALIGN_CENTER, Table.ALIGN_RIGHT, Table.ALIGN_RIGHT);
        builder.addRow(new Text.Strikethrough("Header"), new Text.Code("Code"),
                new Text.Italic("Random"));
        builder.addRow(new Text.Strikethrough("Header"), new Text.Code("Code"),
                new Text.Italic("Random"));
        Table old = builder.build();
        Assertions.assertEquals(old, builder.build());
        builder.addRow(new Text.Strikethrough("Header"), new Text.Code("Code"),
                new Text.Italic("Random"));
        Assertions.assertNotEquals(old, builder.build());
        ;
    }

    /**
     * Название говорит само за себя.
     */
    @Test
    void testEqualsLinkAndImage() {
        Link.Builder builderLink = new Link.Builder();
        Link.Builder builderImage = new Link.Builder();

        builderImage.isImage(true);
        builderImage.textAppend(new Text("this is the link"));
        builderLink.textAppend(new Text("this is the link"));

        Assertions.assertNotEquals(builderImage.build(), builderLink.build());

        builderImage.isImage(false);

        Assertions.assertEquals(builderImage.build(), builderLink.build());

        builderImage.urlReplace("mysite");
        builderLink.urlReplace("URL");

        Assertions.assertNotEquals(builderImage.build(), builderLink.build());
    }

    /**
     * Название говорит само за себя.
     */
    @Test
    void testEqualsList() {
        List.Builder builder1 = new List.Builder();
        List.Builder builder2 = new List.Builder();
        builder1.add(new Text("first"));
        builder2.add(new Text("first"));
        builder1.add(new Text("second"));
        builder1.add(new Text("third"));
        builder2.add(new Text("second"));

        Assertions.assertNotEquals(builder1.build(), builder2.build());

        builder2.add(new Text("third"));

        Assertions.assertEquals(builder1.build(), builder2.build());

        builder1.setTypeOfList(List.Builder.ORDEREDLIST);

        Assertions.assertNotEquals(builder1.build(), builder2.build());
    }

    /**
     * Название говорит само за себя.
     */
    @Test
    void testEqualsText() {
        Text.Builder builder1 = new Text.Builder();
        Text.Builder builder2 = new Text.Builder();

        builder1.append("str");
        builder2.append("str");

        builder1.setType(Text.Builder.HEADING1);
        builder2.setType(Text.Builder.HEADING1);
        Assertions.assertEquals(builder1.build(), builder2.build());
        builder1.setType(Text.Builder.HEADING2);
        Assertions.assertNotEquals(builder1.build(), builder2.build());

        builder1.setType(Text.Builder.CODE);
        builder2.setType(Text.Builder.CODE);
        Assertions.assertEquals(builder1.build(), builder2.build());

        builder1.setType(Text.Builder.STRIKETHROUGH);
        builder2.setType(Text.Builder.STRIKETHROUGH);
        Assertions.assertEquals(builder1.build(), builder2.build());

        builder1.setType(Text.Builder.BOLD);
        builder2.setType(Text.Builder.BOLD);
        Assertions.assertEquals(builder1.build(), builder2.build());

        builder1.setType(Text.Builder.ITALIC);
        builder2.setType(Text.Builder.ITALIC);
        Assertions.assertEquals(builder1.build(), builder2.build());


        builder1.setType(Text.Builder.QUOTE);
        builder2.setType(Text.Builder.QUOTE);
        Assertions.assertEquals(builder1.build(), builder2.build());


        builder1.setType(Text.Builder.CODEBLOCK);
        builder2.setType(Text.Builder.CODEBLOCK);
        Assertions.assertEquals(builder1.build(), builder2.build());

    }
}
