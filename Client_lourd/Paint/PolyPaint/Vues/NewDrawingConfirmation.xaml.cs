
using PolyPaint.Modeles;
using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Ink;
using System.Windows.Input;

namespace PolyPaint.Vues
{
    /// <summary>
    /// Interaction logic for NewDrawingConfirmation.xaml
    /// </summary>
    public partial class NewDrawingConfirmation : UserControl
    {
        public NewDrawingConfirmation()
        {
            InitializeComponent();
     
            //inkPresenter.Strokes.Add(getStrokeCollection());
        }

        private void MessageBoxControl_Loaded(object sender, RoutedEventArgs e)
        {
    
        }

        private void back_Click(object sender, RoutedEventArgs e)
        {

        }
    }
}
