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

    public partial class LoginWindow : UserControl
    {
        public LoginWindow()
        { 
            InitializeComponent();
        }

        public struct player
        {
            public string user, pass;

            public player(string username, string password)
            {
                user = username;
                pass = password;
            }
        }

        private async void UserConnect(object sender, RoutedEventArgs e)
        {
            string username = usernameBox.Text;
            string password = passwordBox.Password;

            var HttpClient = new HttpClient();
            var infos = new player(username, password);

            var json = JsonConvert.SerializeObject(infos);
            //var res = await HttpClient.PostAsync("/players/login", new StringContent(json));
            Console.WriteLine(username);
            Console.WriteLine(password);
        }

        private void usernameBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            if (usernameBox.Text.Length > 0)
                tbuser.Visibility = Visibility.Collapsed;
            else
                tbuser.Visibility = Visibility.Visible;
        }

        private void passwordBox_PasswordChanged(object sender, RoutedEventArgs e)
        {
            if (passwordBox.Password.Length > 0)
                tbpass.Visibility = Visibility.Collapsed;
            else
                tbpass.Visibility = Visibility.Visible;
        }
    }
}