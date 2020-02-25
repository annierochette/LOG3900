using System;
using System.Net.Http;
using Newtonsoft.Json;
using System.Collections.Generic;
using System.Web;
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
using System.Windows.Shapes;

namespace PolyPaint.Vues
{

    public partial class SignInWindow : UserControl
    {
        public SignInWindow()
        {
            InitializeComponent();
        }

        private void surnameBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            if (surnameBox.Text.Length > 0)
                tbsurname.Visibility = Visibility.Collapsed;
            else
                tbsurname.Visibility = Visibility.Visible;
        }
        private void nameBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            if (nameBox.Text.Length > 0)
                tbname.Visibility = Visibility.Collapsed;
            else
                tbname.Visibility = Visibility.Visible;
        }

        private void usernameBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            if (usernameBox.Text.Length > 0)
                tbusername.Visibility = Visibility.Collapsed;
            else
                tbusername.Visibility = Visibility.Visible;
        }

        private void passBox_PasswordChanged(object sender, RoutedEventArgs e)
        {
            if (passBox.Password.Length > 0)
                tbpassword.Visibility = Visibility.Collapsed;
            else
                tbpassword.Visibility = Visibility.Visible;
        }

        private void rePassBox_PasswordChanged(object sender, RoutedEventArgs e)
        {
            if (rePassBox.Password.Length > 0)
                tbrepassword.Visibility = Visibility.Collapsed;
            else
                tbrepassword.Visibility = Visibility.Visible;
        }
    }
}
