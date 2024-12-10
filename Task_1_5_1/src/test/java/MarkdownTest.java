import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.markdown.Link;
import org.markdown.List;
import org.markdown.Table;
import org.markdown.Text;

public class MarkdownTest {
    @Test
    void testImage() {
        Link.Builder builder = new Link.Builder();
        builder.isImage(true);
        builder.textAppend(new Text("this is the link"));
        builder.urlReplace("https://my_site.com");
        Link link = builder.build();
        Assertions.assertEquals("![this is the link](https://my_site.com)", link.toString());
    }

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
        Assertions.assertEquals(builder.build().toString(),
                "|~~Header~~|`Code`|*Random*|\n" +
                        "|:-:|-:|-:|\n" +
                        "|~~Header~~|`Code`|*Random*|\n" +
                        "|~~Header~~|`Code`|*Random*|\n");

    }

    @Test
    void testText() {
        Text.Builder builder = new Text.Builder();
        builder.append(new Text.Heading("HEADING\n", 2));
        builder.append(new Text("Wrong text"));
        builder.remove(1);
        builder.append(new Text.Quote("Quote"));
        builder.append(new Text("\n"));
        builder.append(new Text.CodeBlock("CODE"));
        Assertions.assertEquals(builder.build().toString(), "HEADING\n>Quote\n\n```\nCODE\n```\n");
    }

    @Test
    void testEqualTable() {
        Table.Builder builder = new Table.Builder();
        builder.withCountOfColumn(3);
        builder.withAlignments(Table.ALIGN_CENTER, Table.ALIGN_RIGHT, Table.ALIGN_RIGHT);
        builder.addRow(new Text.Strikethrough("Header"), new Text.Code("Code"), new Text.Italic("Random"));
        builder.addRow(new Text.Strikethrough("Header"), new Text.Code("Code"), new Text.Italic("Random"));
        Table old = builder.build();
        Assertions.assertEquals(old, builder.build());
        builder.addRow(new Text.Strikethrough("Header"), new Text.Code("Code"), new Text.Italic("Random"));
        Assertions.assertNotEquals(old, builder.build());;
    }
}
