(function() {
    'use strict';
    angular
        .module('romaninisistemasApp')
        .factory('Cor', Cor);

    Cor.$inject = ['$resource'];

    function Cor ($resource) {
        var resourceUrl =  'api/cors/:id';

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
