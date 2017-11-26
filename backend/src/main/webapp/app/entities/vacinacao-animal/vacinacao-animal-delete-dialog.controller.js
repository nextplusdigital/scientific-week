(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('VacinacaoAnimalDeleteController',VacinacaoAnimalDeleteController);

    VacinacaoAnimalDeleteController.$inject = ['$uibModalInstance', 'entity', 'VacinacaoAnimal'];

    function VacinacaoAnimalDeleteController($uibModalInstance, entity, VacinacaoAnimal) {
        var vm = this;

        vm.vacinacaoAnimal = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            VacinacaoAnimal.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
