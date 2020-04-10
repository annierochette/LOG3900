using PolyPaint.VueModeles;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;

namespace PolyPaint.Vues
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : UserControl
    {
        public MainWindow()
        {
            InitializeComponent();
            FontFamily = new FontFamily("Microsoft JhengHei UI");
        }

        private void MessageBoxControl_Loaded(object sender, RoutedEventArgs e)
        {

        }

        public void  Disconnect(object sender, RoutedEventArgs e)
        {
            System.Windows.Forms.Application.Restart();
            Application.Current.Shutdown();
        }
    }
}
