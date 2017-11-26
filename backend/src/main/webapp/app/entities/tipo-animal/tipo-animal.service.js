(function() {
    'use strict';
    angular
        .module('romaninisistemasApp')
        .factory('TipoAnimal', TipoAnimal);

    TipoAnimal.$inject = ['$resource'];

    function TipoAnimal ($resource) {
        var resourceUrl =  'api/tipo-animals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
