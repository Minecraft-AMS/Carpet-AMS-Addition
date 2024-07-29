package club.mcams.carpet.helpers.rule.commandPlayerChunkLoadController;

public enum ChunkLoadingLevel {
    NONE(34),
    EDGE(33),
    WEAK(32),
    STRONG(31);

    private final int level;

    ChunkLoadingLevel(int level) {
        this.level = level;
    }

    public int level() {
        return level;
    }
}
