(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('PessoaDetailController', PessoaDetailController);

    PessoaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pessoa', 'User'];

    function PessoaDetailController($scope, $rootScope, $stateParams, previousState, entity, Pessoa, User) {
        var vm = this;

        vm.pessoa = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('romaninisistemasApp:pessoaUpdate', function(event, result) {
            vm.pessoa = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
