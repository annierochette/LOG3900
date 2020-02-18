using PolyPaint.Utilitaires;
using System;
using System.Windows.Input;


namespace PolyPaint.VueModeles
{
   public class GameMenuViewModel : BaseViewModel, IPageViewModel { 
   private ICommand _goToUserProfile;

        public ICommand GoToUserProfile
        {
            get
            {
                return _goToUserProfile ?? (_goToUserProfile = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToUserProfile", "");
                }));
            }
        }

    }
}