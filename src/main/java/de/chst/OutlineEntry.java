package de.chst;

public class OutlineEntry {
    private final String chapterNum;
    private final String description;
    private final int pageNumber;

    public OutlineEntry(String chapterNum, String description, int pageNumber) {
        this.chapterNum = chapterNum;
        this.description = description;
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getChapterNum() {
        return chapterNum;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OutlineEntry) {
            OutlineEntry other = (OutlineEntry) o;

            return other.getChapterNum().equals(getChapterNum()) &&
                    other.getDescription().equals(getDescription()) &&
                    other.getPageNumber() == getPageNumber();
        }

        return false;
    }

    @Override
    public String toString() {
        String result = "(OutlineEntry " +
                this.getChapterNum() + ", " +
                this.getDescription() + ", " +
                this.getPageNumber() + ")";
        return result;
    }
}
