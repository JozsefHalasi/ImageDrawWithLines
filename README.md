# ImageDrawWithLines
Create artistic looking photos from existing source.

## The following steps needs to be done in this order:

- load an image (source image)
- get a list of all the colors in the source image
- create 2 blank images (image1 and image2)
- draw a random polygon or circle on image1 using a random color from source image
- compare image1 to the source image
- if it's closer in color to the source image than image2, copy image1 to image2; if not, copy image2 to image1 and continue drawing more random shapes and comparing
