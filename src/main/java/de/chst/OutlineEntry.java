package de.chst;

public class OutlineEntry {
    private String chapterNum;
    private String description;
    private int pageNumber;

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

    public int getLevel() {
        /* Count the number of dots */
        int found = 0;
        for (char c : chapterNum.toCharArray()) {
            if (c == '.') found++;
        }

        return found;
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
        StringBuilder result = new StringBuilder();
        result.append("(OutlineEntry ");
        result.append(this.getChapterNum()).append(", ");
        result.append(this.getDescription()).append(", ");
        result.append(this.getPageNumber()).append(")");
        return result.toString();
    }
}
