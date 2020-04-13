using System;
using System.Net.Http;
using Newtonsoft.Json;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using PolyPaint.VueModeles;
using PolyPaint.Modeles;
using PolyPaint.Utilitaires;

namespace PolyPaint.Vues
{

    public partial class LoginWindow : UserControl
    {
        public LoginWindow()
        { 
            InitializeComponent();
            User user = User.instance;
        }

        public class loginPlayer
        {

            [JsonProperty("username")]
            public string username { get; set; }

            [JsonProperty("password")]
            public string password { get; set; }
        }

        private void UserConnectOnEnter(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                UserConnect(sender, e);
            }
        }

        private async void UserConnect(object sender, RoutedEventArgs e)
        {
            
            string username = usernameBox.Text;
            string password = passwordBox.Password;

            var HttpClient = new HttpClient();
            var infos = new loginPlayer
            {
                username = username,
                password = password
            };

            var json = await Task.Run(() => JsonConvert.SerializeObject(infos));
            var httpContent = new StringContent(json, Encoding.UTF8, "application/json");
           
            var res = await HttpClient.PostAsync(Constants.ADDR + "players/login", httpContent);
            var responseContent = await res.Content.ReadAsStringAsync();
            Console.WriteLine(responseContent);
            if (responseContent != "{}")
            {
             
                User.instance.Username = username;
                ((LoginViewModel)(DataContext)).GiveAccess();
            } else
            {
                ErreurConnection.Visibility = Visibility.Visible;

            }
        
         
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