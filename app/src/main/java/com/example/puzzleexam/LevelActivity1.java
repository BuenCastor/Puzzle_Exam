package com.example.puzzleexam;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class LevelActivity1 extends AppCompatActivity {
    ArrayList<PuzzlePiece> pieces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1);

     //   final ConstraintLayout layout1 = findViewById(R.id.layout1);
        final RelativeLayout layout = findViewById(R.id.layout);
        ImageView imageView = findViewById(R.id.photo1);

        // запускаем связанный с изображением код после того, как представление было выложено
        // рассчитать все размеры
        imageView.post(new Runnable() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void run() {
                pieces = splitImage();
                TouchListener touchListener = new TouchListener();
//                for (Bitmap piece: pieces){
//                    ImageView iv = new ImageView(getApplicationContext());
//                    iv.setImageBitmap(piece);
//                    iv.setOnTouchListener(touchListener);
//                    layout.addView(iv);
//                }
                for(PuzzlePiece piece : pieces) {
                    piece.setOnTouchListener(touchListener);
                    layout.addView(piece);
                }
            }
        });
    }

    private ArrayList <PuzzlePiece> splitImage() {
        int piecesNumber = 12;         // частей пазла
        int rows = 4;
        int cols = 3;

        ImageView imageView = findViewById(R.id.photo1);
        ArrayList<PuzzlePiece> pieces = new ArrayList<>(piecesNumber);

        // Получить растровое изображение исходного изображения
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int [] sizes = getBitmapPositionInsideImageView(imageView);
        int scaleBitmapLeft = sizes [0];
        int scaleBitmapTop = sizes [1];
        int scaleBitmapWidth = sizes [2];
        int scaleBitmapHeight = sizes [3];

        int croppedImageWidth = scaleBitmapWidth - 2 * Math.abs(scaleBitmapLeft);
        int croppedImageHeight = scaleBitmapHeight - 2 * Math.abs(scaleBitmapTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,scaleBitmapWidth,scaleBitmapHeight,true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, Math.abs(scaleBitmapLeft),Math.abs(scaleBitmapTop),
                croppedImageWidth,croppedImageHeight);

        // Рассчитать ширину и высоту кусков
        int pieceWidth = croppedImageWidth  / cols;
        int pieceHeight = croppedImageHeight / rows;

        // Создаем каждый фрагмент растрового изображения и добавляем его в результирующий массив
        int yCoord = 0;
        for(int row = 0; row < rows; row++){
            int xCoord = 0;
            for(int col = 0; col < cols; col++){
              //  pieces.add(Bitmap.createBitmap(bitmap,xCoord,yCoord,pieceWidth,pieceHeight));
               // Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord, yCoord, pieceWidth, pieceHeight);
                int offsetX = 0;
                int offsetY = 0;
                if (col > 0) {
                    offsetX = pieceWidth / 3;
                }
                if (row > 0) {
                    offsetY = pieceHeight / 3;
                }

                // apply the offset to each piece
                Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord - offsetX, yCoord - offsetY, pieceWidth + offsetX, pieceHeight + offsetY);
                PuzzlePiece piece = new PuzzlePiece(getApplicationContext());
                piece.setImageBitmap(pieceBitmap);
//                piece.xCoord = xCoord + imageView.getLeft();
//                piece.yCoord = yCoord + imageView.getTop();
//                piece.pieceWidth = pieceWidth;
//                piece.pieceHeight = pieceHeight;
                piece.xCoord = xCoord - offsetX + imageView.getLeft();
                piece.yCoord = yCoord - offsetY + imageView.getTop();
                piece.pieceWidth = pieceWidth + offsetX;
                piece.pieceHeight = pieceHeight + offsetY;

                // this bitmap will hold our final puzzle piece image
                Bitmap puzzlePiece = Bitmap.createBitmap(pieceWidth + offsetX, pieceHeight + offsetY, Bitmap.Config.ARGB_8888);

                // draw path
                int bumpSize = pieceHeight / 4;
                Canvas canvas = new Canvas(puzzlePiece);
                Path path = new Path();
                path.moveTo(offsetX, offsetY);
                if (row == 0) {
                    // top side piece
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                } else {
                    // top bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3, offsetY);
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, offsetY);
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                }

                if (col == cols - 1) {
                    // right side piece
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                } else {
                    // right bump
                    path.lineTo(pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.cubicTo(pieceBitmap.getWidth() - bumpSize,offsetY + (pieceBitmap.getHeight() - offsetY) / 6, pieceBitmap.getWidth() - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5, pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                }

                if (row == rows - 1) {
                    // bottom side piece
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                } else {
                    // bottom bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, pieceBitmap.getHeight());
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5,pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6, pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3, pieceBitmap.getHeight());
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                }

                if (col == 0) {
                    // left side piece
                    path.close();
                } else {
                    // left bump
                    path.lineTo(offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.cubicTo(offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5, offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6, offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.close();
                }

                // mask the piece
                Paint paint = new Paint();
                paint.setColor(0XFF000000);
                paint.setStyle(Paint.Style.FILL);

                canvas.drawPath(path, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(pieceBitmap, 0, 0, paint);

                // draw a white border
                Paint border = new Paint();
                border.setColor(0X80FFFFFF);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(8.0f);
                canvas.drawPath(path, border);

                // draw a black border
                border = new Paint();
                border.setColor(0X80000000);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(3.0f);
                canvas.drawPath(path, border);

                // set the resulting bitmap to the piece
                piece.setImageBitmap(puzzlePiece);
                pieces.add(piece);
                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }
        return  pieces;
    }

    private int [] getBitmapPositionInsideImageView (ImageView imageView){
        int [] ret = new int [4];

        if(imageView == null || imageView.getDrawable() == null)
            return ret;

        // Получить размеры изображения
        // Получить значения матрицы изображения и поместить их в массив
        float [] f = new float [9];
        imageView.getImageMatrix().getValues(f);

        // Извлекаем значения масштаба, используя константы (если соотношение сторон сохранено, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Получить отрисовку (также можно получить растровое изображение за отрисовкой и getWidth / getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        //Расчет фактических размеров
        final int actW = Math.round(origW*scaleX);
        final int actH = Math.round(origH* scaleY);

        ret [2] = actW;
        ret [3] = actH;

        //Получить положение изображения
        // Мы предполагаем, что изображение центрировано в ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH) / 2;
        int left = (int) (imgViewW - actW) / 2;

        ret [0] = left;
        ret [1] = top;

        return ret;
    }
}
