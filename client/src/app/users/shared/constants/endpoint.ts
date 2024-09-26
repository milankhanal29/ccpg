
export const Endpoint = {
  categories: '/api/product-categories',
  products: '/api/products',
  getAllProducts: '/api/products/get-all-product',
  addProduct: '/api/products/save-product',
  saveOrder: '/api/orders/save-order',
  getAllOrders: 'api/orders/get-all-orders',
  orderBaseUrl: '/api/orders',
  auth: '/api/auth',
  carts: '/carts',
  getCartForUser: (userId: number) => `${Endpoint.carts}/${userId}`,
  addItemToCart:'/carts/add',
  updateCartItem: `/carts/update`,
  removeCartItem: (id: number) => `${Endpoint.carts}/remove/${id}`,
  clearCart: (userId: number) => `${Endpoint.carts}/clear/${userId}`,
  getUserByEmail:'/user/get-userid-by-email',
  getReceiverById:'/api/receiver/id/',
};
