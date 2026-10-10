package terfusti.mcextract.export;

import java.nio.file.Path;

public record ExportResult(int exported, int failed, String error, Path outputDirectory) {
    public String summary() {
        if (error != null) return "Export failed (see log)";
        if (failed > 0) return "Exported " + exported + ", failed " + failed;
        return "Exported " + exported + " skin(s)";
    }
}
