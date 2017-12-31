# ImageDrawWithLines
Create artistic looking photos from existing source.

## Usage

Compile java files:
javac ImageDrawWithLines.java

Run:
java ImageDrawWithLines 'input_image' 'output_image' 'iteration_number'

(log.txt is auto generated)

## The following steps needs to be done in this order:

- load an image (source image)
- get a list of all the colors in the source image
- create 2 blank images (image1 and image2)
- draw a random polygon or circle on image1 using a random color from source image
- compare image1 to the source image
- if it's closer in color to the source image than image2, copy image1 to image2; if not, copy image2 to image1 and continue drawing more random shapes and comparing
