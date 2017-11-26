(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('TipoAnimalDeleteController',TipoAnimalDeleteController);

    TipoAnimalDeleteController.$inject = ['$uibModalInstance', 'entity', 'TipoAnimal'];

    function TipoAnimalDeleteController($uibModalInstance, entity, TipoAnimal) {
        var vm = this;

        vm.tipoAnimal = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TipoAnimal.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
