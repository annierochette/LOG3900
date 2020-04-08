using Newtonsoft.Json;
using PolyPaint.Modeles;
using PolyPaint.Utilitaires;
using System;
using System.Collections.Generic;
using System.Data.Linq;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Windows.Input;

namespace PolyPaint.VueModeles
{
    public class UserProfileViewModel : BaseViewModel, IPageViewModel
    {
        public UserProfileViewModel()
        {
            
        }

        User user = User.Instance;

        public override string GetCurrentViewModelName()
        {
            return "UserProfileViewModel";
        }

        public string Username
        {
            get { return user.Username; }
            set { user.Username = value; }
        }

        public string Firstname
        {
            get { return user.Firstname; }
            set { user.Lastname = value; }
        }

        public string Lastname
        {
            get { return user.Firstname; }
            set { user.Lastname = value; }
        }

        private async Task getPlayerInfoAsync()
        {
            try
            {
                Console.WriteLine(Username);
                var HttpClient = new HttpClient();
                HttpResponseMessage response = await HttpClient.GetAsync("http://localhost:5050/players/" + Username);
                response.EnsureSuccessStatusCode();
                string res = await response.Content.ReadAsStringAsync();

                //var json = await Task.Run(() => JsonConvert.SerializeObject(infos));
                //var httpContent = new StringContent(json, Encoding.UTF8, "application/json");

                //var res = await HttpClient.PostAsync("http://localhost:5050/games", httpContent);
                //if (res.Content != null)
                //{
                //    var responseContent = await res.Content.ReadAsStringAsync();
                //    Console.WriteLine(responseContent);
                //    System.Windows.Forms.MessageBox.Show("Image bien sauvegardée!", "Caption", MessageBoxButtons.OK, MessageBoxIcon.Information);

                //}
            }

            catch (Exception ex)
            {
                System.Windows.Forms.MessageBox.Show(ex.Message);
            }
        }


        private ICommand _goToGameMenu;

        public ICommand GoToGameMenu
        {
            get
            {
                return _goToGameMenu ?? (_goToGameMenu = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToGameMenu", "");
                }));
            }
        }

    }
}
