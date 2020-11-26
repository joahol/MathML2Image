# MathML2Image
This tool's converts MathML stroke data to images. Primarily designed for creating trainingdata for MachineLearning algorithms.
Images are created as Binary black and white, with a predefined size of 28x28 pixels, which is configurable. 
There is two tool's one for generating images of single symbols, and one tool for generating images of entire mathematical expressions.

Features:
- batch export of all symbols in a MathML file.
- batch export of all MathML files in a directory.

Configuration:
- Create a instantanse of the parser.
- Assign the path to MathML files.
- Configure scaling size of the symbols.
- Configure output size of the images.

No binaries are supplied for this project.
