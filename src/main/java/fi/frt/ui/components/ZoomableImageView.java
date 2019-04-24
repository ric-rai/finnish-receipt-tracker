package fi.frt.ui.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;

public class ZoomableImageView extends VBox {
    private ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();
    private final int MIN_PIXELS = 10;
    private Image image = new Image(new ByteArrayInputStream(new byte[0]));
    private ImageView imageView;
    private byte[] imageArray;

    public ZoomableImageView() {
        Pane container = new Pane();
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(container.widthProperty());
        imageView.fitHeightProperty().bind(container.heightProperty());
        container.getChildren().add(imageView);
        this.getChildren().add(0, container);
        this.setFillWidth(true);
        VBox.setVgrow(container, Priority.ALWAYS);

        imageView.setOnMousePressed(e -> {
            Point2D mousePress = imageViewToImage(new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });

        imageView.setOnMouseDragged(e -> {
            Point2D dragPoint = imageViewToImage(new Point2D(e.getX(), e.getY()));
            shift(dragPoint.subtract(mouseDown.get()));
            mouseDown.set(imageViewToImage(new Point2D(e.getX(), e.getY())));
        });

        imageView.setOnScroll(e -> {
            double delta = e.getDeltaY();
            Rectangle2D viewport = imageView.getViewport();

            double scale = clamp(Math.pow(1.005, delta),  // altered the value from 1.01to zoom slower
                    // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
                    // don't scale so that we're bigger than image dimensions:
                    Math.max(image.getWidth() / viewport.getWidth(), image.getHeight() / viewport.getHeight())
            );
            if (scale != 1.0) {
                Point2D mouse = imageViewToImage(new Point2D(e.getX(), e.getY()));

                double newWidth = viewport.getWidth();
                double newHeight = viewport.getHeight();
                double imageViewRatio = (imageView.getFitWidth() / imageView.getFitHeight());
                double viewportRatio = (newWidth / newHeight);
                if (viewportRatio < imageViewRatio) {
                    // adjust width to be proportional with height
                    newHeight = newHeight * scale;
                    newWidth = newHeight * imageViewRatio;
                    if (newWidth > image.getWidth()) {
                        newWidth = image.getWidth();
                    }
                } else {
                    // adjust height to be proportional with width
                    newWidth = newWidth * scale;
                    newHeight = newWidth / imageViewRatio;
                    if (newHeight > image.getHeight()) {
                        newHeight = image.getHeight();
                    }
                }

                // To keep the visual point under the mouse from moving, we need
                // (x - newViewportMinX) / (x - currentViewportMinX) = scale
                // where x is the mouse X coordinate in the image
                // solving this for newViewportMinX gives
                // newViewportMinX = x - (x - currentViewportMinX) * scale
                // we then clamp this value so the image never scrolls out
                // of the imageview:
                double newMinX = 0;
                if (newWidth < image.getWidth()) {
                    newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
                            0, image.getWidth() - newWidth);
                }
                double newMinY = 0;
                if (newHeight < image.getHeight()) {
                    newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
                            0, image.getHeight() - newHeight);
                }

                imageView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
            }
        });

        imageView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                reset(getWidth() / 2, getHeight() / 2);
            }
        });
    }

    public void initialize() {
        initButtons();
        this.setWidth(this.getMinWidth());
        this.setHeight(this.getMinHeight());
        reset(getWidth() / 2, getHeight() / 2);
    }

    private void initButtons() {
        Button reset = (Button) this.lookup("#resetView");
        Button full = (Button) this.lookup("#fullView");
        reset.setOnAction(e -> reset(getWidth() / 2, getHeight() / 2));
        full.setOnAction(e -> reset(image.getWidth(), image.getHeight()));
    }

    private void reset(double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width + 50, height + 50));
    }

    private void shift(Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();
        double width = imageView.getImage().getWidth();
        double height = imageView.getImage().getHeight();
        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();
        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);
        if (minX < 0.0) minX = 0.0;
        if (minY < 0.0) minY = 0.0;
        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    private double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    // convert mouse coordinates in the imageView to coordinates in the actual image:
    private Point2D imageViewToImage(Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();
        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }

    public void setZoomableImage(byte[] imageArray) {
        this.imageArray = imageArray;
        Image image = new Image(new ByteArrayInputStream(imageArray));
        imageView.setImage(image);
        this.image = image;
        reset(getWidth() / 2, getHeight() / 2);
    }

    public byte[] getImage(){
        return imageArray;
    }

}
