#!/bin/sh
target=$(dirname "$0")/ref.pdf
curl "https://www.adobe.com/content/dam/acom/en/devnet/pdf/pdfs/pdf_reference_archives/PDFReference.pdf" -o "$target"