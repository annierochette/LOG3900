using PolyPaint.Utilitaires;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PolyPaint.VueModeles
{
    public class MainWindowViewModel : BaseViewModel
    {
        private IPageViewModel _currentPageViewModel;
        private List<IPageViewModel> _pageViewModels;

        public List<IPageViewModel> PageViewModels
        {
            get
            {
                if (_pageViewModels == null)
                    _pageViewModels = new List<IPageViewModel>();

                return _pageViewModels;
            }
        }

        public IPageViewModel CurrentPageViewModel
        {
            get
            {
                return _currentPageViewModel;
            }
            set
            {
                _currentPageViewModel = value;
                OnPropertyChanged("CurrentPageViewModel");
            }
        }

        private void ChangeViewModel(IPageViewModel viewModel)
        {
            if (!PageViewModels.Contains(viewModel))
                PageViewModels.Add(viewModel);

            CurrentPageViewModel = PageViewModels
                .FirstOrDefault(vm => vm == viewModel);
        }

        private void OnGoToGameMenu(object obj)
        {
            ChangeViewModel(PageViewModels[0]);
        }

        private void OnGoToUserProfile(object obj)
        {
            ChangeViewModel(PageViewModels[1]);
        }

        public MainWindowViewModel()
        {
            
            // Add available pages and set page
            PageViewModels.Add(new GameMenuViewModel());
            PageViewModels.Add(new UserProfileViewModel());

            CurrentPageViewModel = PageViewModels[0];

            Mediator.Subscribe("GoToGameMenu", OnGoToGameMenu);
            Mediator.Subscribe("GoToUserProfile", OnGoToUserProfile);
        }
    }
}
