using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;

namespace PolyPaint.Modeles
{
    public sealed class User : INotifyPropertyChanged
    {
        public static User instance = null;
        private static readonly object padlock = new object();

        public static User Instance
        {
            get
            {
                lock (padlock)
                {
                    if (instance == null)
                    {
                        instance = new User();
                    }
                    return instance;
                }
            }
        }

        private string username;

        public string Username
        {
            get { return username; }
            set
            {
                username = value;
                ChangeProperty();
            }
        }

        private string firstname;

        public string Firstname
        {
            get { return firstname; }
            set
            {
                firstname = value;
                ChangeProperty();
            }
        }

        private string lastname;

        public string Lastname
        {
            get { return lastname; }
            set
            {
                lastname = value;
                ChangeProperty();
            }
        }

        public event PropertyChangedEventHandler PropertyChanged;


        protected void ChangeProperty([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }

}
