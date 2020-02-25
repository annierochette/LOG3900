
using PolyPaint.Modeles;
using System.Windows.Controls;


namespace PolyPaint.Vues
{
    /// <summary>
    /// Interaction logic for Window1.xaml
    /// </summary>
    public partial class GameMenu : UserControl
    {
        public GameMenu()
        {
            InitializeComponent();
            var model = new Message()
            {
               MessageList = "Bienvenue à la messagerie!",
            };

            //DataContext = model;
        }

        private void MessageBoxControl_Loaded(object sender, System.Windows.RoutedEventArgs e)
        {

        }
    }
}
