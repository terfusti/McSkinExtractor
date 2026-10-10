package terfusti.mcextract.export;

public record ExportResult(int exported, int failed, String error) {
    public String summary() {
        if (error != null) return "Export failed (see log)";
        if (failed > 0) return "Exported " + exported + ", failed " + failed;
        return "Exported " + exported + " skin(s)";
    }
}
