//using Prototype_Lourd.Model;
//using Prototype_Lourd.RelayCommand;
//using System.ComponentModel;
//using System.Runtime.CompilerServices;

//namespace Prototype_lourd.VueModel
//{
//    class VueModel : INotifyPropertyChanged
//    {
//        public event PropertyChangedEventHandler PropertyChanged;
//        private UserConnection user = new UserConnection();

//        public string Username
//        {
//            get { return user.Username; }
//            set { ModifiedProperty(); }
//        }

//        public string IPAddress
//        {
//            get { return user.IPAddress; }
//            set { ModifiedProperty(); }
//        }


//        public RelayCommand<string> ChooseUsername { get; set; }
//        public RelayCommand<string> SelectIpAddress{ get; set; }
//        public VueModel()
//        {
//            // On écoute pour des changements sur le modèle. Lorsqu'il y en a, UserModifiedProperty est appelée.
//            PropertyChanged += new PropertyChangedEventHandler(UserModifiedProperty);

//            ChooseUsername = new RelayCommand<string>(user.ChooseUsername);
//            SelectIpAddress = new RelayCommand<string>(user.SelectIpAddress);
           
//        }

//        private void UserModifiedProperty(object sender, PropertyChangedEventArgs e)
//        {
//            if (e.PropertyName == "Username")
//            {
//                Username = user.Username;
//            }
//            if (e.PropertyName == "IPAddress")
//            {
//                IPAddress = user.IPAddress;
//            }
         
//        }
//        protected virtual void ModifiedProperty([CallerMemberName] string propertyName = null)
//        {
//            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
//        }

//    }
//}
