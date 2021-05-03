# PDF Outline Editor

Allows you to add an outline (or table of contents) to a pdf file.
It overwrites existing entries, so its more of a Replacer than an Editor.

## Build

`./gradlew shadowJar`

The jar will be in the `build/libs/` folder.

## Features

- Read outline entries from a text file
- Specify page labels instead of numbers
- Replace PDF file in-place
- Deduce tree structure of outline from numbering scheme

  
## Usage

`pdfoutlineeditor.jar [--resolve-page-labels] [--output OUTFILE] INFILE OUTLINEFILE`

Example:
```
$ cat > outline.txt
1 First entry..........6
1.1 Another one........7
2 Chapter 2............9
$ java -jar pdfoutlineeditor.jar my.pdf outline.txt
```


The outline text file must adhere to the following specification:
1. An optional chapter specification consisting of numbers and dots (1, 1.1, 1.1.2 and so on) separated with a single
   space from
2. the description (the actual label of the outline entry) separated with two or more dots from
3. the page number. If you specify the `--resolve-page-labels` option, you can also use page numbers as displayed by the
   PDF viewer, such as roman numerals for the preamble.
