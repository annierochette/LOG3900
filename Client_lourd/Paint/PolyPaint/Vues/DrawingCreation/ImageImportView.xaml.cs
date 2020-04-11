using Microsoft.Win32;
using PolyPaint.VueModeles;
using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media.Imaging;
using CsPotrace;
using System.Collections;
using System.Drawing;
using System.Windows.Forms;
using OpenFileDialog = Microsoft.Win32.OpenFileDialog;
using Svg;
using System.IO;

namespace PolyPaint.Vues
{
    /// <summary>
    /// Interaction logic for ImageImportView.xaml
    /// </summary>
    public partial class ImageImportView : System.Windows.Controls.UserControl
    {
        bool[,] Matrix;
        ArrayList ListOfCurveArray;
        Bitmap Bitmap;

        public ImageImportView()
        {
            InitializeComponent();
            save_button.IsEnabled = false;
        }

        private void btnLoad_Click(object sender, RoutedEventArgs e)
        {

           
            OpenFileDialog op = new OpenFileDialog();
            op.Title = "Select a picture";
            op.Filter = "All supported graphics|*.jpg;*.jpeg;*.png|" +
              "JPEG (*.jpg;*.jpeg)|*.jpg;*.jpeg|" +
              "Portable Network Graphic (*.png)|*.png" +
              "|BMP Windows Bitmap (*.bmp)|*.bmp";
         
            if (op.ShowDialog() == true)
            {
                imgPhoto.Source = new BitmapImage(new Uri(op.FileName));
                save_button.IsEnabled = true;
                ListOfCurveArray = null;
                if (Bitmap != null) Bitmap.Dispose();
                Bitmap = new Bitmap(op.FileName);
                refreshMatrix();
            }
          
        }

        private void refreshMatrix()
        {
            if (Bitmap == null) return;
            Matrix = Potrace.BitMapToBinary(Bitmap, (int)contrastSlider.Value);
            refreshPicture();

        }

        private void ContrastSlider_Scroll(object sender, EventArgs e)
        {
            refreshMatrix();
            float p = 100 * (float)contrastSlider.Value / (float)255;

        }

        private void refreshPicture()
        {
            if (Matrix == null) return;
            Bitmap b = Potrace.BinaryToBitmap(Matrix, true);
            imgPhoto.Source = BitmapToImageSource(b);

        }

        private void vectorize()
        {

            ListOfCurveArray = new ArrayList();
            Potrace.turdsize = Convert.ToInt32(contrastSlider.Value);
            Potrace.curveoptimizing = true;
            Matrix = Potrace.BitMapToBinary(Bitmap, (int)contrastSlider.Value);
            Potrace.potrace_trace(Matrix, ListOfCurveArray);
            Bitmap s = Potrace.Export2GDIPlus(ListOfCurveArray, Bitmap.Width, Bitmap.Height);
            imgPhoto.Source = BitmapToImageSource(s);
            refreshMatrix();

        }

        BitmapImage BitmapToImageSource(Bitmap bitmap)
        {
            using (MemoryStream memory = new MemoryStream())
            {
                bitmap.Save(memory, System.Drawing.Imaging.ImageFormat.Bmp);
                memory.Position = 0;
                BitmapImage bitmapimage = new BitmapImage();
                bitmapimage.BeginInit();
                bitmapimage.StreamSource = memory;
                bitmapimage.CacheOption = BitmapCacheOption.OnLoad;
                bitmapimage.EndInit();

                return bitmapimage;
            }
        }
        private void saveAsSVG(object sender, EventArgs e)
        {
            vectorize();
            System.Windows.Forms.SaveFileDialog saveFileDialog = new System.Windows.Forms.SaveFileDialog();
            saveFileDialog.DefaultExt = "svg";
            saveFileDialog.FileName = "*.svg";
            saveFileDialog.Filter = "svg files (*.svg)|*.svg";

        

            if (saveFileDialog.ShowDialog() == DialogResult.OK)
            {

                // le fichier svg a envoyer à la bd.
                string s = Potrace.Export2SVG(ListOfCurveArray, Bitmap.Width, Bitmap.Height);
                // on ne gardera pas ça mais c'est utile pareil
                System.IO.StreamWriter FS = new System.IO.StreamWriter(saveFileDialog.FileName);
                FS.Write(s);
                FS.Close();
            }

        }

        private void save_button_Click(object sender, RoutedEventArgs e)
        {

        }
    }
}
