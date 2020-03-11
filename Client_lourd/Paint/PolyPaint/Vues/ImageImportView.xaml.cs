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
            }
            ListOfCurveArray = null;
            if (Bitmap != null) Bitmap.Dispose();
            Bitmap = new Bitmap(op.FileName);
            vectorize();
        }

      
         //private void refreshMatrix()
         //{
         //    if (Bitmap == null) return;
         //    Matrix = Potrace.BitMapToBinary(Bitmap, 130);
             
         //}

    private void vectorize()
        {

            ListOfCurveArray = new ArrayList();
            Potrace.turdsize = Convert.ToInt32(255);
            //Potrace.alphamax = Convert.ToDouble(textBox5.Text);
            //Potrace.opttolerance = Convert.ToDouble(textBox3.Text);
      
            //optimize the path p, replacing sequences of Bezier segments by a
            //single segment when possible.
            Potrace.curveoptimizing = true;
            Matrix = Potrace.BitMapToBinary(Bitmap, 255);
            Potrace.potrace_trace(Matrix, ListOfCurveArray);
       


        }

        private void saveAsSVG(object sender, EventArgs e)
        {
        
            System.Windows.Forms.SaveFileDialog saveFileDialog = new System.Windows.Forms.SaveFileDialog();
            saveFileDialog.DefaultExt = "svg";
            saveFileDialog.FileName = "*.svg";
            saveFileDialog.Filter = "svg files (*.svg)|*.svg";

            if (saveFileDialog.ShowDialog() == DialogResult.OK)
            {
                string s = Potrace.Export2SVG(ListOfCurveArray, Bitmap.Width, Bitmap.Height);
                System.IO.StreamWriter FS = new System.IO.StreamWriter(saveFileDialog.FileName);
                FS.Write(s);
                FS.Close();
            }

        }
    }
}
