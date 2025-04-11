package io.cdap.directives.row;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.DirectiveContext;
import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.util.List;
import java.util.ArrayList;

public class AggregateStats implements Directive {
    private String byteCol;
    private String timeCol;
    private String outByteCol;
    private String outTimeCol;
    private long totalBytes = 0;
    private long totalTime = 0;
    private int rowCount = 0;

    @Override
    public void initialize(DirectiveContext context, Arguments args) {
        byteCol = args.value("byteCol");
        timeCol = args.value("timeCol");
        outByteCol = args.value("outByteCol");
        outTimeCol = args.value("outTimeCol");
    }

    @Override
    public List<Row> execute(List<Row> rows) {
        for (Row row : rows) {
            ByteSize bs = new ByteSize(row.getValue(byteCol).toString());
            TimeDuration td = new TimeDuration(row.getValue(timeCol).toString());
            totalBytes += bs.getBytes();
            totalTime += td.getMilliseconds();
            rowCount++;
        }

        List<Row> result = new ArrayList<>();
        Row out = new Row();
        out.add(outByteCol, totalBytes / (1024 * 1024));  // MB
        out.add(outTimeCol, totalTime / 1000.0);  // Seconds
        result.add(out);
        return result;
    }

    @Override
    public UsageDefinition define() {
        return UsageDefinition.builder()
            .define("byteCol", TokenType.COLUMN_NAME)
            .define("timeCol", TokenType.COLUMN_NAME)
            .define("outByteCol", TokenType.COLUMN_NAME)
            .define("outTimeCol", TokenType.COLUMN_NAME)
            .build();
    }
}