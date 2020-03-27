using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace PolyPaint.Vues
{
    /// <summary>
    /// Logique d'interaction pour UserControl1.xaml
    /// </summary>
    public partial class GameChoiceWindow : UserControl
    {
        public GameChoiceWindow()
        {
            InitializeComponent();
        }

        private void MessageBoxControl_Loaded(object sender, RoutedEventArgs e)
        {

        }

        private void testing(object sender, RoutedEventArgs e) {
        //    Window window = new Window
        //    {
        //        Title = "My User Control Dialog",
        //        Content = new LoginWindow(),
        //        SizeToContent = SizeToContent.WidthAndHeight,
        //        ResizeMode = ResizeMode.NoResize
        //    };
        //    window.ShowDialog();
        }
    }
}
