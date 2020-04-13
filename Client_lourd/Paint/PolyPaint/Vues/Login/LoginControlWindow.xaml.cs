using System.Windows;
using System.Windows.Media;

namespace PolyPaint.Vues
{
    /// <summary>
    /// Interaction logic for LoginControlWindow.xaml
    /// </summary>
    public partial class LoginControlWindow : Window
    {
        public LoginControlWindow()
        {
            InitializeComponent();
            FontFamily = new FontFamily("Microsoft JhengHei UI");
           // FontFamily = new FontFamily("Ink Free");
        }
    }
}
